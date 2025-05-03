package com.hau.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TinNhanLopCreateRequest {
    private Integer lopHocId;
    private String noiDung;
}