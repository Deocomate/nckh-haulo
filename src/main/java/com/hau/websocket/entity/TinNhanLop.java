// --- TinNhanLop.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Entity đại diện cho Tin nhắn trong một Lớp học.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tin_nhan_lop")
public class TinNhanLop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết nhiều-một với LopHoc (tin nhắn thuộc về lớp học nào)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lop_hoc_id", nullable = false)
    private LopHoc lopHoc;

    // Liên kết nhiều-một với NguoiDung (người gửi tin nhắn)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_gui_id", nullable = false)
    private NguoiDung nguoiGui;

    // Nội dung tin nhắn dạng text
    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    // URL của tệp đính kèm (nếu có)
    @Column(name = "dinh_kem_url", length = 500)
    private String dinhKemUrl;

    // Tên gốc của tệp đính kèm
    @Column(name = "dinh_kem_ten_goc", length = 255)
    private String dinhKemTenGoc;

    // Loại MIME của tệp đính kèm
    @Column(name = "dinh_kem_loai", length = 100)
    private String dinhKemLoai;

    // Thời gian gửi, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "thoi_gian_gui", nullable = false, updatable = false)
    private Timestamp thoiGianGui;
}