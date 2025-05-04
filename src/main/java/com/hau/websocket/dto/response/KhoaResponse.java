package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhoaResponse {
    private Integer id;
    private String maKhoa;
    private String tenKhoa;
}