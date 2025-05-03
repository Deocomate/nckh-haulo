// --- PhongBan.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Entity đại diện cho Phòng ban.
 * Có mối quan hệ OneToMany với CanBoPhongBan.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phong_ban")
public class PhongBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Mã phòng ban, là duy nhất và không null
    @Column(name = "ma_phong_ban", unique = true, nullable = false, length = 50)
    private String maPhongBan;

    // Tên phòng ban, không null
    @Column(name = "ten_phong_ban", nullable = false, length = 255)
    private String tenPhongBan;

    // Mô tả về phòng ban
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    // Email liên hệ của phòng ban
    @Column(name = "email_lien_he", length = 255)
    private String emailLienHe;

    // Số điện thoại liên hệ của phòng ban
    @Column(name = "so_dien_thoai_lien_he", length = 20)
    private String soDienThoaiLienHe;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;

    // Danh sách Cán bộ thuộc Phòng ban này (OneToMany)
    // Đã đổi tên biến từ 'canBoPhongBans' thành 'danhSachCanBoPhongBan'
    @OneToMany(mappedBy = "phongBan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CanBoPhongBan> danhSachCanBoPhongBan;
}