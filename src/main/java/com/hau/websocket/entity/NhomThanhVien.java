// --- NhomThanhVien.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Entity đại diện cho mối quan hệ thành viên trong nhóm (bảng trung gian).
 * Sử dụng khóa chính tự tăng đơn giản thay vì khóa phức hợp.
 * Lưu ý: Tên lớp này có thể không nhất quán với tên bảng "thanh_vien_nhom".
 * Xem xét đổi tên lớp thành ThanhVienNhom cho đồng bộ.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "thanh_vien_nhom") // Ánh xạ tới bảng thanh_vien_nhom
public class NhomThanhVien { // Tên lớp giữ nguyên theo file gốc, nhưng nên đổi thành ThanhVienNhom

    // Khóa chính tự tăng đơn giản
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết nhiều-một với Nhom
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhom_id", nullable = false)
    private Nhom nhom;

    // Liên kết nhiều-một với NguoiDung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    // Vai trò của thành viên trong nhóm (ví dụ: ThanhVien, QuanTriVien)
    @Column(name = "vai_tro_trong_nhom", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'ThanhVien'")
    private String vaiTroTrongNhom = "ThanhVien";

    // Thời gian tham gia, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tham_gia", nullable = false, updatable = false)
    private Timestamp ngayThamGia;
}