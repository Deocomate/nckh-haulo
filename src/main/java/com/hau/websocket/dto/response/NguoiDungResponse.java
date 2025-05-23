package com.hau.websocket.dto.response;

import com.hau.websocket.entity.LoaiNguoiDung;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiDungResponse {
    private Integer id;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String diaChi;
    private String anhDaiDien;

}
