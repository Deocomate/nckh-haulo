package com.hau.websocket.service;

import com.hau.websocket.dto.request.ChangePasswordRequest;
import com.hau.websocket.dto.request.NguoiDungCreateRequest;
import com.hau.websocket.dto.request.NguoiDungUpdateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.NguoiDungResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.entity.NguoiDung;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.NguoiDungMapper;
import com.hau.websocket.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NguoiDungService {
    private final NguoiDungRepository nguoiDungRepository;
    private final NguoiDungMapper nguoiDungMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Value("${app.file.download-prefix}")
    private String fileDownloadPrefix;

    public ApiResponse<NguoiDungResponse> createUser(NguoiDungCreateRequest nguoiDungCreateRequest) {
        NguoiDung nguoiDung = nguoiDungMapper.toNguoiDung(nguoiDungCreateRequest);
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDungCreateRequest.getMatKhau()));
        try {
            nguoiDungRepository.save(nguoiDung);

            return ApiResponse.<NguoiDungResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Tạo người dùng thành công")
                    .result(null)
                    .timestamp(LocalDateTime.now())
                    .build();
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Tài khoản đã tồn tại", null);
        }
    }

    public ApiResponse<PageResponse<NguoiDungResponse>> getAllUsers(int page, int size) {
        Sort sort = Sort.by("ngayTao").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<NguoiDung> userPage = nguoiDungRepository.findAll(pageable);

        List<NguoiDungResponse> userResponseList = userPage.map(nguoiDungMapper::toNguoiDungResponse).toList();
        userResponseList.forEach(userResponse -> userResponse.setAnhDaiDien(fileDownloadPrefix + userResponse.getAnhDaiDien()));
        return ApiResponse.<PageResponse<NguoiDungResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách user thành công")
                .result(PageResponse.<NguoiDungResponse>builder()
                        .currentPage(page)
                        .totalPages(userPage.getTotalPages())
                        .totalElements(userPage.getTotalElements())
                        .pageSize(userPage.getSize())
                        .data(userResponseList)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<NguoiDungResponse> myInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();

        NguoiDung user = findUserById(Integer.valueOf(id));
        NguoiDungResponse userResponse = nguoiDungMapper.toNguoiDungResponse(user);
        userResponse.setAnhDaiDien(fileDownloadPrefix + user.getAnhDaiDien());
        return ApiResponse.<NguoiDungResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin thành công")
                .result(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public ApiResponse<NguoiDungResponse> updateUserProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage == null || profileImage.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Vui lòng chọn ảnh để tải lên", null);
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        NguoiDung user = findUserById(Integer.valueOf(authentication.getName()));

        var fileResponse = fileService.uploadFile(profileImage);
        user.setAnhDaiDien(fileResponse.getUrl());
        nguoiDungRepository.save(user);
        return ApiResponse.<NguoiDungResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật ảnh đại diện thành công")
                .result(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<NguoiDungResponse> updateUser(Integer id, NguoiDungUpdateRequest userUpdateRequest) {
        NguoiDung user = findUserById(id);
        nguoiDungMapper.toNguoiDungUpdateRequest(user, userUpdateRequest);
        user.setMatKhau(passwordEncoder.encode(userUpdateRequest.getMatKhau()));

        nguoiDungRepository.save(user);
        return ApiResponse.<NguoiDungResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật thông tin user thành công")
                .result(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<NguoiDungResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer id = Integer.valueOf(authentication.getName());
        NguoiDung user = findUserById(id);
        if (!passwordEncoder.matches(changePasswordRequest.getMatKhauCu(), user.getMatKhau())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu cũ không đúng", null);
        }

        user.setMatKhau(passwordEncoder.encode(changePasswordRequest.getMatKhauMoi()));
        nguoiDungRepository.save(user);
        return ApiResponse.<NguoiDungResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Đổi mật khẩu thành công")
                .result(null)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public NguoiDung findUserById(Integer id) {
        return nguoiDungRepository
                .findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng có id: " + id, null));
    }
}
