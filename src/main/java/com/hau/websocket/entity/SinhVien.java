
// --- SinhVienService.java ---
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


    @JoinColumn(name = "nguoi_dung_id", unique = true, nullable = false)
    private Integer nguoiDungId;

    // Mã sinh viên, là duy nhất và không null
    @Column(name = "ma_sinh_vien", unique = true, nullable = false, length = 50)
    private String maSinhVien;

    @JoinColumn(name = "lop_hoc_id", nullable = false)
    private Integer lopHocId;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;
}