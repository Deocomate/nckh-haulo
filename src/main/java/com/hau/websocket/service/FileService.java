package com.hau.websocket.service;

import com.hau.websocket.dto.FileData;
import com.hau.websocket.dto.response.FileResponse;
import com.hau.websocket.entity.FileManagement;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.FileManagementMapper;
import com.hau.websocket.repository.FileManagementRepository;
import com.hau.websocket.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileManagementRepository fileManagementRepository;
    private final FileManagementMapper fileManagementMapper;

    public FileResponse uploadFile(MultipartFile file) throws IOException {
        var fileInfo = fileRepository.uploadFile(file);
        var fileManagement = fileManagementMapper.toFileManagement(fileInfo);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setOwnerId(userId);
        fileManagement.setCreatedAt(LocalDateTime.now());
        fileManagementRepository.save(fileManagement);
        return (FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(fileInfo.getFileUrl())
                .contentType(fileInfo.getContentType())
                .build());
    }

    public FileData downloadFile(String fileName) throws IOException {
        var fileManagement = fileManagementRepository.findById(fileName).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "File không tồn tại", null)
        );

        return new FileData(fileManagement.getContentType(), fileRepository.read(fileManagement));
    }

    public void deleteFile(String fileName) throws IOException {
        fileRepository.delete(fileName);
    }

    public FileManagement getFileManagement(String fileName) {
        return fileManagementRepository.findById(fileName)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "File không tồn tại", null));
    }
}
