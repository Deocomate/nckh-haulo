package com.hau.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NganhResponse {
    private Integer id;
    private String maNganh;
    private String tenNganh;
    private KhoaResponse khoa;
}