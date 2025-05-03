
// --- SinhVien.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

/**
 * Entity đại diện cho Sinh viên.
 * Liên kết OneToOne với NguoiDung và ManyToOne với LopHoc.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    // Khóa chính tự tăng đơn giản
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết một-một với NguoiDung (nguoi_dung_id là unique và không null)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", unique = true, nullable = false)
    private NguoiDung nguoiDung;

    // Mã sinh viên, là duy nhất và không null
    @Column(name = "ma_sinh_vien", unique = true, nullable = false, length = 50)
    private String maSinhVien;

    // Liên kết nhiều-một với LopHoc (mỗi sinh viên thuộc về một lớp học)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lop_hoc_id", nullable = false)
    private LopHoc lopHoc;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;
}