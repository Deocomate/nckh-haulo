package com.hau.websocket.controller;

import com.hau.websocket.dto.request.TinNhanLopCreateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.TinNhanLopResponse;
import com.hau.websocket.service.TinNhanLopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class-messages")
public class TinNhanLopController {
    private final TinNhanLopService tinNhanLopService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTinNhanLop(
            @RequestPart("tinNhan") TinNhanLopCreateRequest tinNhanLopCreateRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile
    ) throws IOException {
        tinNhanLopService.createTinNhanLop(tinNhanLopCreateRequest, multipartFile);
    }

    @GetMapping("/{lopHocId}")
    public ResponseEntity<ApiResponse<PageResponse<TinNhanLopResponse>>> getAllTinNhanLopByLopHocId(
            @PathVariable Integer lopHocId,
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        ApiResponse<PageResponse<TinNhanLopResponse>> apiResponse = tinNhanLopService.getTinNhanLopByLopHocId(pageIndex, pageSize, lopHocId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{tinNhanLopId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTinNhanLop(@PathVariable Integer tinNhanLopId) {
        tinNhanLopService.deleteTinNhanLop(tinNhanLopId);
    }

    @DeleteMapping("/class/{lopHocId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTinNhanLopByLopHocId(@PathVariable Integer lopHocId) {
        tinNhanLopService.deleteAllTinNhanLopByLopHocId(lopHocId);
    }
}