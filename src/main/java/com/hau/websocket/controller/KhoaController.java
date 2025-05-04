package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.KhoaResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.service.KhoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/khoa")
@RequiredArgsConstructor
public class KhoaController {

    private final KhoaService khoaService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<KhoaResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String maKhoa,
            @RequestParam(required = false) String tenKhoa
    ) {
        ApiResponse<PageResponse<KhoaResponse>> apiResponse = khoaService.getAll(
                pageIndex,
                pageSize,
                maKhoa,
                tenKhoa
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhoaResponse>> getById(@PathVariable Integer id) {
        ApiResponse<KhoaResponse> apiResponse = khoaService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}