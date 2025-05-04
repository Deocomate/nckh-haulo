package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.ThanhVienNhomResponse;
import com.hau.websocket.service.ThanhVienNhomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/thanh-vien-nhom")
@RequiredArgsConstructor
public class ThanhVienNhomController {

    private final ThanhVienNhomService thanhVienNhomService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ThanhVienNhomResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer nhomId,
            @RequestParam(required = false) Integer nguoiDungId,
            @RequestParam(required = false) String vaiTroTrongNhom
    ) {
        ApiResponse<PageResponse<ThanhVienNhomResponse>> apiResponse = thanhVienNhomService.getAll(
                pageIndex,
                pageSize,
                nhomId,
                nguoiDungId,
                vaiTroTrongNhom
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ThanhVienNhomResponse>> getById(@PathVariable Integer id) {
        ApiResponse<ThanhVienNhomResponse> apiResponse = thanhVienNhomService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}