package com.hau.websocket.controller;

import com.hau.websocket.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        var fileData = fileService.downloadFile(fileName);
        var fileManagement = fileService.getFileManagement(fileName);

        String originalFileName = fileManagement.getId();
        String contentType = fileData.contentType();
        if (contentType == null) contentType = "application/octet-stream";

        // Các loại content type cho phép hiển thị trên browser
        boolean isInlineDisplay =
                contentType.startsWith("image/")
                        || contentType.startsWith("video/");

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType);

        if (!isInlineDisplay) {
            // Các loại file khác sẽ ép tải về
            responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + originalFileName + "\"");
        }
        return responseBuilder.body(fileData.resource());
    }

    @DeleteMapping("/{fileName}")
    public void deleteFile(@PathVariable String fileName) throws IOException {
        fileService.deleteFile(fileName);
    }
}
