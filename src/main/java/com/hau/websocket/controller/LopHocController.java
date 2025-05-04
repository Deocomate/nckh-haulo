package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.LopHocResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.service.LopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lop-hoc")
@RequiredArgsConstructor
public class LopHocController {

    private final LopHocService lopHocService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<LopHocResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String maLop,
            @RequestParam(required = false) String tenLop,
            @RequestParam(required = false) String nienKhoa,
            @RequestParam(required = false) Integer nganhId
    ) {
        ApiResponse<PageResponse<LopHocResponse>> apiResponse = lopHocService.getAll(
                pageIndex,
                pageSize,
                maLop,
                tenLop,
                nienKhoa,
                nganhId
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LopHocResponse>> getById(@PathVariable Integer id) {
        ApiResponse<LopHocResponse> apiResponse = lopHocService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}