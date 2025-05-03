package com.hau.websocket.dto.request;

import com.hau.websocket.entity.LoaiNguoiDung;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiDungCreateRequest {
    @Pattern(regexp = "^[a-z0-9]+$", message = "Tài khoản chỉ được chứa chữ cái thường và không có ký tự đặc biệt")
    private String maDangNhap;
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String matKhau;
    private String hoTen;
    @Email(message = "Email không đúng định dạng")
    private String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
    private String soDienThoai;
    private LoaiNguoiDung loaiTaiKhoan;
    private String diaChi;
    private String anhDaiDien;
}
