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
public class ThanhVienNhomResponse {
    private Integer id;
    private Integer nhomId;
    private Integer nguoiDungId;
    private String vaiTroTrongNhom;
    private LocalDateTime ngayThamGia;
}