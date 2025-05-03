package com.hau.websocket.controller;

import com.hau.websocket.dto.request.ChangePasswordRequest;
import com.hau.websocket.dto.request.NguoiDungCreateRequest;
import com.hau.websocket.dto.request.NguoiDungUpdateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.NguoiDungResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.service.NguoiDungService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class NguoiDungController {
    private final NguoiDungService nguoiDungService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> createUser(
            @Valid @RequestBody NguoiDungCreateRequest userCreateRequest) {
        ApiResponse<NguoiDungResponse> userResponse = nguoiDungService.createUser(userCreateRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> myInfo() {
        ApiResponse<NguoiDungResponse> userResponse = nguoiDungService.myInfo();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> updateUser(
            @PathVariable Integer userId, @Valid @RequestBody NguoiDungUpdateRequest userUpdateRequest) {
        ApiResponse<NguoiDungResponse> userResponse = nguoiDungService.updateUser(userId, userUpdateRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> updatePassword( @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        ApiResponse<NguoiDungResponse> userResponse = nguoiDungService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NguoiDungResponse>>> getAllUsers(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        ApiResponse<PageResponse<NguoiDungResponse>> userPage = nguoiDungService.getAllUsers(pageIndex, pageSize);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @PutMapping("/profile-image")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> updateUserProfileImage(
            @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        ApiResponse<NguoiDungResponse> userResponse = nguoiDungService.updateUserProfileImage(profileImage);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
