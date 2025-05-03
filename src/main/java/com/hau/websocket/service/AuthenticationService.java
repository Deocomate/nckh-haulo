package com.hau.websocket.service;

import com.hau.websocket.dto.request.AuthenticationRequest;
import com.hau.websocket.dto.request.IntrospectRequest;
import com.hau.websocket.dto.request.LogoutRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.AuthenticationResponse;
import com.hau.websocket.dto.response.IntrospectResponse;
import com.hau.websocket.entity.InvalidatedToken;
import com.hau.websocket.entity.NguoiDung;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.repository.InvalidatedTokenRepository;
import com.hau.websocket.repository.NguoiDungRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
    private final NguoiDungRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        NguoiDung nguoiDung = userRepository
                .findByMaDangNhap(authenticationRequest.getMaDangNhap())
                .orElse(null);

        if (nguoiDung == null || !passwordEncoder.matches(authenticationRequest.getMatKhau(), nguoiDung.getMatKhau())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Tên đăng nhập hoặc mật khẩu không chính xác", null);
        }

        String token;
        try {
            token = tokenService.generateToken(nguoiDung);
        } catch (JOSEException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi tạo token", e);
        }

        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .result(AuthenticationResponse.builder()
                        .authenticated(true)
                        .token(token)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<Void> logout(LogoutRequest logoutRequest) throws ParseException {
        var signToken = tokenService.verifyToken(logoutRequest.getToken(), true);
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryDate(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Đăng xuất thành công")
                .result(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<IntrospectResponse> introspect(IntrospectRequest introspectRequest) {
        String token = introspectRequest.getToken();

        if (token == null || token.isEmpty()) {
            return buildErrorResponse("Token không được cung cấp");
        }

        try {
            // Sử dụng TokenService thay vì logic tại chỗ
            IntrospectResponse result = tokenService.validateToken(token);

            if (result.isValid()) {
                return ApiResponse.<IntrospectResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Token hợp lệ")
                        .result(result)
                        .timestamp(LocalDateTime.now())
                        .build();
            } else {
                return buildErrorResponse("Token không hợp lệ");
            }
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage());
        }
    }

    private ApiResponse<IntrospectResponse> buildErrorResponse(String message) {
        return ApiResponse.<IntrospectResponse>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .result(IntrospectResponse.builder().valid(false).build())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
