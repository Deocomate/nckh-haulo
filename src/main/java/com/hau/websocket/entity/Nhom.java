// --- Nhom.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Entity đại diện cho Nhóm (có thể là nhóm tùy chỉnh hoặc nhóm lớp học).
 * Có mối quan hệ ManyToOne với NguoiDung (người tạo) và OneToMany với ThanhVienNhom, TinNhanNhom.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhom")
public class Nhom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên nhóm, không null
    @Column(name = "ten_nhom", nullable = false, length = 255)
    private String tenNhom;

    // Mô tả về nhóm
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    // URL hoặc đường dẫn ảnh đại diện của nhóm
    @Column(name = "anh_dai_dien_nhom", length = 500)
    private String anhDaiDienNhom;

    // Liên kết nhiều-một với NguoiDung (người tạo nhóm)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao_id", nullable = false)
    private NguoiDung nguoiTao;

    // Loại nhóm (ví dụ: CLB, DuAn, HocTap, TuyChinh), mặc định là "TuyChinh"
    @Builder.Default
    @Column(name = "loai_nhom", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'TuyChinh'")
    private String loaiNhom = "TuyChinh";

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;

    // Danh sách Thành viên thuộc Nhóm này (OneToMany)
    // Đã đổi tên biến từ 'thanhVienNhoms' thành 'danhSachThanhVienNhom'
    @OneToMany(mappedBy = "nhom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ThanhVienNhom> danhSachThanhVienNhom;

    // Danh sách Tin nhắn thuộc Nhóm tùy chỉnh này (OneToMany)
    // Đã đổi tên biến từ 'tinNhanNhoms' thành 'danhSachTinNhanNhom'
    @OneToMany(mappedBy = "nhom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TinNhanNhom> danhSachTinNhanNhom;
}