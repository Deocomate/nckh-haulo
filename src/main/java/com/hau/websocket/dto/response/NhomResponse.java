package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhomResponse {
    private Integer id;
    private String tenNhom;
    private String moTa;
    private String anhDaiDienNhom;
    private Integer nguoiTaoId;
    private String loaiNhom;
}