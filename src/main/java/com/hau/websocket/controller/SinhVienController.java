package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.SinhVienResponse;
import com.hau.websocket.service.SinhVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sinh-vien")
@RequiredArgsConstructor
public class SinhVienController {

    private final SinhVienService sinhVienService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SinhVienResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String maSinhVien,
            @RequestParam(required = false) Integer nguoiDungId,
            @RequestParam(required = false) Integer lopHocId
    ) {
        ApiResponse<PageResponse<SinhVienResponse>> apiResponse = sinhVienService.getAll(
                pageIndex,
                pageSize,
                maSinhVien,
                nguoiDungId,
                lopHocId
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SinhVienResponse>> getById(@PathVariable Integer id) {
        ApiResponse<SinhVienResponse> apiResponse = sinhVienService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}