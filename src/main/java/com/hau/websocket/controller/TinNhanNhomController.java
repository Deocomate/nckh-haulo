package com.hau.websocket.controller;

import com.hau.websocket.dto.request.TinNhanNhomCreateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.TinNhanNhomResponse;
import com.hau.websocket.service.TinNhanNhomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group-messages")
public class TinNhanNhomController {
    private final TinNhanNhomService tinNhanNhomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTinNhanNhom(
            @RequestPart("tinNhan") TinNhanNhomCreateRequest tinNhanNhomCreateRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile
    ) throws IOException {
        tinNhanNhomService.createTinNhanNhom(tinNhanNhomCreateRequest, multipartFile);
    }

    @GetMapping("/{nhomId}")
    public ResponseEntity<ApiResponse<PageResponse<TinNhanNhomResponse>>> getAllTinNhanNhomByNhomId(
            @PathVariable Integer nhomId,
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        ApiResponse<PageResponse<TinNhanNhomResponse>> apiResponse = tinNhanNhomService.getTinNhanNhomByNhomId(pageIndex, pageSize, nhomId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{tinNhanNhomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTinNhanNhom(@PathVariable Integer tinNhanNhomId) {
        tinNhanNhomService.deleteTinNhanNhom(tinNhanNhomId);
    }

    @DeleteMapping("/group/{nhomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTinNhanNhomByNhomId(@PathVariable Integer nhomId) {
        tinNhanNhomService.deleteAllTinNhanNhomByNhomId(nhomId);
    }
}