package com.hau.websocket.service;

import com.hau.websocket.dto.request.RefreshTokenRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.AuthenticationResponse;
import com.hau.websocket.dto.response.IntrospectResponse;
import com.hau.websocket.entity.InvalidatedToken;
import com.hau.websocket.entity.NguoiDung;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.repository.InvalidatedTokenRepository;
import com.hau.websocket.repository.NguoiDungRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expirationMinutes}")
    private Long expiration;

    @Value("${jwt.expirationRefreshMinutes}")
    private Long expirationRefresh;

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final NguoiDungRepository nguoiDungRepository;

    private JWTClaimsSet validateTokenClaims(String token, boolean isRefresh) throws AppException {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            if (!signedJWT.verify(verifier)) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Chữ ký token không hợp lệ", null);
            }

            Date expirationTime = (isRefresh)
                    ? new Date(claimsSet
                    .getIssueTime()
                    .toInstant()
                    .plus(expirationRefresh, ChronoUnit.MINUTES)
                    .toEpochMilli())
                    : claimsSet.getExpirationTime();

            if (expirationTime == null || expirationTime.before(new Date())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Token đã hết hạn", null);
            }

            if (claimsSet.getIssuer() == null || !claimsSet.getIssuer().equals(issuer)) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Token issuer không hợp lệ", null);
            }

            if (claimsSet.getIssueTime() != null && claimsSet.getIssueTime().after(new Date())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Token chưa có hiệu lực", null);
            }

            if (invalidatedTokenRepository.existsById(claimsSet.getJWTID())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Token đã hết hiệu lực", null);
            }

            return claimsSet;

        } catch (ParseException | JOSEException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token không hợp lệ", null);
        }
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws AppException {
        try {
            validateTokenClaims(token, isRefresh);
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token không hợp lệ", null);
        }
    }

    public IntrospectResponse validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return IntrospectResponse.builder().valid(false).build(); // Use the builder directly
        }
        try {
            JWTClaimsSet claimsSet = validateTokenClaims(token, false);
            return IntrospectResponse.builder()
                    .valid(true)
                    .id(Integer.valueOf(claimsSet.getSubject()))
                    .build();
        } catch (AppException e) {
            return IntrospectResponse.builder().valid(false).build(); // Consistent error handling
        }
    }

    public String generateToken(NguoiDung nguoiDung) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(nguoiDung.getId().toString())
                .claim("ma_dang_nhap", nguoiDung.getMaDangNhap())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(expiration, ChronoUnit.MINUTES).toEpochMilli()))
//                .claim("scope", buildScope(nguoiDung))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(signerKey.getBytes()));
        return jwsObject.serialize();
    }

    public ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest)
            throws JOSEException, ParseException {
        SignedJWT signedJWT = verifyToken(refreshTokenRequest.getToken(), true);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        invalidatedTokenRepository.save(
                InvalidatedToken.builder().id(jwtId).expiryDate(expiryTime).build());

        Integer maNguoiDung = Integer.valueOf(signedJWT.getJWTClaimsSet().getSubject());
        log.info("Refresh token for nguoiDung: {}", maNguoiDung);
        NguoiDung nguoiDung = nguoiDungRepository
                .findById(maNguoiDung)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Người dùng không tồn tại", null));

        String token = generateToken(nguoiDung);
        log.info("Refresh token: {}", token);

        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Làm mới token thành công")
                .result(AuthenticationResponse.builder()
                        .authenticated(true)
                        .token(token)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Scheduled(fixedRate = 1440, timeUnit = TimeUnit.MINUTES)
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        List<InvalidatedToken> expiredTokens = invalidatedTokenRepository.findByExpiryDateLessThanEqual(date);
        invalidatedTokenRepository.deleteAll(expiredTokens);
        log.info("Cleaned up {} expired tokens.", expiredTokens.size());
    }
}
