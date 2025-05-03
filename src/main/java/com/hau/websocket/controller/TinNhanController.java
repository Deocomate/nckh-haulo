package com.hau.websocket.controller;

import com.hau.websocket.dto.request.TinNhanCreateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.TinNhanResponse;
import com.hau.websocket.service.TinNhanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class TinNhanController {
    private final TinNhanService tinNhanService;

    @PostMapping
    public void createMessage(
            @RequestPart("tinNhan") TinNhanCreateRequest tinNhanCreateRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile
    ) throws IOException {
        tinNhanService.crateTinNhan(tinNhanCreateRequest, multipartFile);
    }

    @GetMapping("/{cuocTroChuyenId}")
    public ResponseEntity<ApiResponse<PageResponse<TinNhanResponse>>> getAllMessagesByCuocTroChuyenId(
            @PathVariable Integer cuocTroChuyenId,
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        ApiResponse<PageResponse<TinNhanResponse>> apiResponse = tinNhanService.getTinNhanByCuocTroChuyenId(pageIndex, pageSize, cuocTroChuyenId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{tinNhanId}")
    public void deleteMessage(@PathVariable Integer tinNhanId) {
        tinNhanService.deleteTinNhan(tinNhanId);
    }

    @DeleteMapping("/conversation/{cuocTroChuyenId}")
    public void deleteMessagesByCuocTroChuyenId(@PathVariable Integer cuocTroChuyenId) {
        tinNhanService.deleteAllTinNhan(cuocTroChuyenId);
    }
}
