// --- NguoiDung.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Entity đại diện cho Người dùng chung trong hệ thống.
 * Bao gồm thông tin cơ bản và các mối quan hệ với các vai trò cụ thể (SinhVien, CanBoPhongBan)
 * và các hoạt động (tạo nhóm, tham gia nhóm, gửi/nhận tin nhắn).
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Mã đăng nhập (MSV, Mã Cán bộ, Username), là duy nhất và không null
    @Column(name = "ma_dang_nhap", unique = true, nullable = false, length = 100)
    private String maDangNhap;

    // Mật khẩu đã được mã hóa
    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;

    // Họ tên người dùng, không null
    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;

    // Email, là duy nhất
    @Column(name = "email", unique = true, length = 255)
    private String email;

    // Số điện thoại
    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    // Địa chỉ
    @Column(name = "dia_chi", length = 500)
    private String diaChi;

    // URL hoặc đường dẫn ảnh đại diện
    @Column(name = "anh_dai_dien", length = 500)
    private String anhDaiDien;

    // Loại tài khoản (SINH_VIEN, CAN_BO_LOP, CAN_BO_PHONG_BAN)
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_tai_khoan", nullable = false)
    private LoaiNguoiDung loaiTaiKhoan;

    // Trạng thái tài khoản (true: active, false: inactive), mặc định là true
    @Builder.Default
    @Column(name = "trang_thai", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean trangThai = true;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;

    // --- Relationships ---

    // Liên kết một-một với SinhVien (nếu là sinh viên)
    @OneToOne(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private SinhVien sinhVien;

    // Liên kết một-một với CanBoPhongBan (nếu là cán bộ phòng ban)
    @OneToOne(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private CanBoPhongBan canBoPhongBan;

    // Danh sách các Nhóm do người dùng này tạo (OneToMany)
    // Tên biến giữ nguyên vì đã đúng quy tắc
    @OneToMany(mappedBy = "nguoiTao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Nhom> danhSachNhomTao;

    // Danh sách các Nhóm mà người dùng này là thành viên (thông qua bảng ThanhVienNhom) (OneToMany)
    // Tên biến giữ nguyên vì đã đúng quy tắc
    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ThanhVienNhom> danhSachThanhVienNhom;

    // Danh sách Tin nhắn 1-1 do người dùng này gửi (OneToMany)
    // Đã đổi tên biến từ 'tinNhansGui' thành 'danhSachTinNhanGui'
    @OneToMany(mappedBy = "nguoiGui", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TinNhan> danhSachTinNhanGui;

    // Danh sách Tin nhắn 1-1 mà người dùng này nhận (OneToMany)
    // Đã đổi tên biến từ 'tinNhansNhan' thành 'danhSachTinNhanNhan'
    @OneToMany(mappedBy = "nguoiNhan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TinNhan> danhSachTinNhanNhan;

    // Danh sách Tin nhắn trong Nhóm tùy chỉnh do người dùng này gửi (OneToMany)
    // Đã đổi tên biến từ 'tinNhanNhomsGui' thành 'danhSachTinNhanNhomGui'
    @OneToMany(mappedBy = "nguoiGui", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TinNhanNhom> danhSachTinNhanNhomGui;

    // Danh sách Tin nhắn trong Lớp học do người dùng này gửi (OneToMany)
    // Đã đổi tên biến từ 'tinNhanLopsGui' thành 'danhSachTinNhanLopGui'
    @OneToMany(mappedBy = "nguoiGui", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TinNhanLop> danhSachTinNhanLopGui;
}