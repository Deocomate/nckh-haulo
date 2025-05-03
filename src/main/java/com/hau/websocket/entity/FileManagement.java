package com.hau.websocket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_management")
@Entity
public class FileManagement {
    @Id
    private String id;
    private String ownerId;
    private String contentType;
    private long size;
    private String md5Checksum;
    private String filePath;

    private LocalDateTime createdAt;
}
