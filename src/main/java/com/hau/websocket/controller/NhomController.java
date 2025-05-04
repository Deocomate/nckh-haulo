package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.NhomResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.service.NhomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nhom")
@RequiredArgsConstructor
public class NhomController {

    private final NhomService nhomService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NhomResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String tenNhom,
            @RequestParam(required = false) String loaiNhom,
            @RequestParam(required = false) Integer nguoiTaoId
    ) {
        ApiResponse<PageResponse<NhomResponse>> apiResponse = nhomService.getAll(
                pageIndex,
                pageSize,
                tenNhom,
                loaiNhom,
                nguoiTaoId
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NhomResponse>> getById(@PathVariable Integer id) {
        ApiResponse<NhomResponse> apiResponse = nhomService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}