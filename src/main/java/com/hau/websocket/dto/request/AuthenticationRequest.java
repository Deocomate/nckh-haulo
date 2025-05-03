package com.hau.websocket.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String maDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;
}
