package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CanBoPhongBanResponse {
    private Integer id;
    private NguoiDungResponse nguoiDung;
    private String maCanBo;
    private PhongBanResponse phongBan;
    private String vaiTroNhanVien;
    private boolean laDauMoiLienLac;
}
