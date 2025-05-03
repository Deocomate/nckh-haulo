package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TinNhanLopResponse {
    private Integer id;
    private Integer lopHocId;
    private Integer nguoiGuiId;
    private String noiDung;
    private String dinhKemUrl;
    private String dinhKemTenGoc;
    private String dinhKemLoai;
    private LocalDateTime thoiGianGui;
}