package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.PhongBanResponse;
import com.hau.websocket.service.PhongBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phong-ban")
@RequiredArgsConstructor
public class PhongBanController {

    private final PhongBanService phongBanService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PhongBanResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String maPhongBan,
            @RequestParam(required = false) String tenPhongBan,
            @RequestParam(required = false) String emailLienHe,
            @RequestParam(required = false) String soDienThoaiLienHe
    ) {
        ApiResponse<PageResponse<PhongBanResponse>> apiResponse = phongBanService.getAll(
                pageIndex,
                pageSize,
                maPhongBan,
                tenPhongBan,
                emailLienHe,
                soDienThoaiLienHe
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PhongBanResponse>> getById(@PathVariable Integer id) {
        ApiResponse<PhongBanResponse> apiResponse = phongBanService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}