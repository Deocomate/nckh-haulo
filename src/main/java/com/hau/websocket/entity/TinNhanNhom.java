// --- TinNhanNhom.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Entity đại diện cho Tin nhắn trong một Nhóm tùy chỉnh.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tin_nhan_nhom") // Tên bảng đã được đổi
public class TinNhanNhom { // Tên lớp đã được đổi

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name = "nhom_id", nullable = false) // Cột join đã được đổi
    private Integer nhomId;


    @JoinColumn(name = "nguoi_gui_id", nullable = false)
    private Integer nguoiGuiId;

    // Nội dung tin nhắn dạng text
    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    // URL của tệp đính kèm (nếu có)
    @Column(name = "dinh_kem_url", length = 500)
    private String dinhKemUrl;

    // Tên gốc của tệp đính kèm
    @Column(name = "dinh_kem_ten_goc", length = 255) // Tên cột đã được đổi
    private String dinhKemTenGoc;

    // Loại MIME của tệp đính kèm
    @Column(name = "dinh_kem_loai", length = 100)
    private String dinhKemLoai;

    // Thời gian gửi, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "thoi_gian_gui", nullable = false, updatable = false)
    private Timestamp thoiGianGui;
}
