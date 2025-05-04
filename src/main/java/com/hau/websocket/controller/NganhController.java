package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.NganhResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.service.NganhService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nganh")
@RequiredArgsConstructor
public class NganhController {

    private final NganhService nganhService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NganhResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String maNganh,
            @RequestParam(required = false) String tenNganh,
            @RequestParam(required = false) String tenKhoa
    ) {
        ApiResponse<PageResponse<NganhResponse>> apiResponse = nganhService.getAll(
                pageIndex,
                pageSize,
                maNganh,
                tenNganh,
                tenKhoa
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NganhResponse>> getById(@PathVariable Integer id) {
        ApiResponse<NganhResponse> apiResponse = nganhService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}