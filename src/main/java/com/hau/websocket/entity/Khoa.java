// --- Khoa.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Entity đại diện cho Khoa.
 * Có mối quan hệ OneToMany với Nganh.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "khoa")
public class Khoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Mã khoa, là duy nhất và không null
    @Column(name = "ma_khoa", unique = true, nullable = false, length = 50)
    private String maKhoa;

    // Tên khoa, không null
    @Column(name = "ten_khoa", nullable = false, length = 255)
    private String tenKhoa;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;

    // Danh sách các Ngành thuộc Khoa này (OneToMany)
    // Đã đổi tên biến từ 'nganhs' thành 'danhSachNganh'
    @OneToMany(mappedBy = "khoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Nganh> danhSachNganh;
}