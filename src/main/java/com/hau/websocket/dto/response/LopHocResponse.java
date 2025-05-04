package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LopHocResponse {
    private Integer id;
    private String maLop;
    private String tenLop;
    private String nienKhoa;
    private NganhResponse nganh;
}