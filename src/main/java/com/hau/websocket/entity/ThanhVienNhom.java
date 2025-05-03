// --- ThanhVienNhom.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Entity đại diện cho mối quan hệ thành viên trong nhóm (bảng trung gian).
 * Sử dụng khóa chính tự tăng đơn giản.
 * Đây là entity chính xác hơn cho bảng "thanh_vien_nhom".
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "thanh_vien_nhom")
public class ThanhVienNhom {

    // Khóa chính tự tăng đơn giản
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name = "nhom_id", nullable = false)
    private Integer nhomId;


    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private Integer nguoiDungId;

    // Vai trò của thành viên trong nhóm (ví dụ: ThanhVien, QuanTriVien)
    @Column(name = "vai_tro_trong_nhom", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'ThanhVien'")
    private String vaiTroTrongNhom = "ThanhVien";

    // Thời gian tham gia, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tham_gia", nullable = false, updatable = false)
    private Timestamp ngayThamGia;
}