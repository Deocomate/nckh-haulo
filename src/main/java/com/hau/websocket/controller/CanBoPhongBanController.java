package com.hau.websocket.controller;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.CanBoPhongBanResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.service.CanBoPhongBanService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/can-bo-phong-ban")
@RequiredArgsConstructor
public class CanBoPhongBanController {

    private final CanBoPhongBanService canBoPhongBanService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CanBoPhongBanResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String tenPhongBan,
            @RequestParam(required = false) String vaiTroNhanVien,
            @RequestParam(required = false) String maCanBo,
            @RequestParam(required = false) Boolean laDauMoiLienLac
    ) {
        ApiResponse<PageResponse<CanBoPhongBanResponse>> apiResponse = canBoPhongBanService.getAll(
                pageIndex,
                pageSize,
                tenPhongBan,
                vaiTroNhanVien,
                maCanBo,
                laDauMoiLienLac
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CanBoPhongBanResponse>> getById(@PathVariable Integer id) {
        ApiResponse<CanBoPhongBanResponse> apiResponse = canBoPhongBanService.getOne(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}