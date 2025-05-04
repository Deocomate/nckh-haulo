package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhongBanResponse {
    private Integer id;
    private String maPhongBan;
    private String tenPhongBan;
    private String emailLienHe;
    private String soDienThoaiLienHe;
}
