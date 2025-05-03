// --- Nganh.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Entity đại diện cho Ngành học.
 * Có mối quan hệ ManyToOne với Khoa và OneToMany với LopHoc.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nganh")
public class Nganh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Mã ngành, là duy nhất và không null
    @Column(name = "ma_nganh", unique = true, nullable = false, length = 50)
    private String maNganh;

    // Tên ngành, không null
    @Column(name = "ten_nganh", nullable = false, length = 255)
    private String tenNganh;

    // Liên kết nhiều-một với Khoa (mỗi ngành thuộc về một khoa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;

    // Danh sách các Lớp học thuộc Ngành này (OneToMany)
    // Đã đổi tên biến từ 'lopHocs' thành 'danhSachLopHoc'
    @OneToMany(mappedBy = "nganh", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<LopHoc> danhSachLopHoc;
}