package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "can_bo_phong_ban")
public class CanBoPhongBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name = "nguoi_dung_id", unique = true, nullable = false)
    private Integer nguoiDungId;

    // Mã cán bộ, là duy nhất và không null
    @Column(name = "ma_can_bo", unique = true, nullable = false, length = 50)
    private String maCanBo;


    @JoinColumn(name = "phong_ban_id", nullable = false)
    private Integer phongBanId;

    // Vai trò của nhân viên trong phòng ban
    @Column(name = "vai_tro_nhan_vien", length = 100)
    private String vaiTroNhanVien;

    // Cờ xác định có phải là đầu mối liên lạc không (mặc định là false)
    @Column(name = "la_dau_moi_lien_lac", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean laDauMoiLienLac = false;

    // Thời gian tạo, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private Timestamp ngayTao;

    // Thời gian cập nhật, tự động gán khi cập nhật
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false)
    private Timestamp ngayCapNhat;
}
