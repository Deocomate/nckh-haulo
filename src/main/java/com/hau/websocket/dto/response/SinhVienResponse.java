package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienResponse {
    private Integer id;
    private Integer nguoiDungId;
    private String maSinhVien;
    private Integer lopHocId;
}