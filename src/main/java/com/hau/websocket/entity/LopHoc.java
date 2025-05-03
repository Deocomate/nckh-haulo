// --- LopHoc.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Entity đại diện cho Lớp học.
 * Có mối quan hệ ManyToOne với Nganh và OneToMany với SinhVien, TinNhanLop.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lop_hoc")
public class LopHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Mã lớp, là duy nhất và không null
    @Column(name = "ma_lop", unique = true, nullable = false, length = 50)
    private String maLop;

    // Tên lớp, không null
    @Column(name = "ten_lop", nullable = false, length = 255)
    private String tenLop;

    // Niên khóa của lớp (ví dụ: 2021-2025)
    @Column(name = "nien_khoa", length = 50)
    private String nienKhoa;

    // Liên kết nhiều-một với Nganh (mỗi lớp thuộc về một ngành)
    @JoinColumn(name = "nganh_id", nullable = false)
    private Integer nganhId;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;
}