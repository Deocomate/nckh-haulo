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
public class TinNhanResponse {
    private Integer id;
    private String cuocTroChuyenId;
    private Integer nguoiGuiId;
    private Integer nguoiNhanId;
    private String noiDung;
    private String dinhKemUrl;
    private String dinhKemTenGoc;
    private String dinhKemLoai;
    private LocalDateTime thoiGianGui;
}
