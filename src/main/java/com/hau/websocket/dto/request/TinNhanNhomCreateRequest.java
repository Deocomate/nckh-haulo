package com.hau.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TinNhanNhomCreateRequest {
    private Integer nhomId;
    private String noiDung;
}