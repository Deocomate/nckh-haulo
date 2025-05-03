// --- TinNhan.java ---
package com.hau.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

/**
 * Entity đại diện cho Tin nhắn 1-1 giữa hai người dùng.
 * Sử dụng cơ chế xóa mềm (soft delete).
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tin_nhan")
// Định nghĩa câu lệnh SQL để cập nhật cờ soft_deleted khi gọi delete()
@SQLDelete(sql = "UPDATE tin_nhan SET soft_deleted = true WHERE id=?")
// Luôn thêm điều kiện soft_deleted=false vào các câu lệnh SELECT
@SQLRestriction("soft_deleted=false")
public class TinNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ID định danh cuộc trò chuyện (ví dụ: user1Id_user2Id sắp xếp theo thứ tự)
    @Column(name = "cuoc_tro_chuyen_id", nullable = false, length = 100)
    private String cuocTroChuyenId;

    // Liên kết nhiều-một với NguoiDung (người gửi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_gui_id", nullable = false)
    private NguoiDung nguoiGui;

    // Liên kết nhiều-một với NguoiDung (người nhận)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_nhan_id", nullable = false)
    private NguoiDung nguoiNhan;

    // Nội dung tin nhắn dạng text
    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    // URL của tệp đính kèm (nếu có)
    @Column(name = "dinh_kem_url", length = 500)
    private String dinhKemUrl;

    // Tên gốc của tệp đính kèm
    @Column(name = "dinh_kem_ten_goc", length = 255) // Đổi tên cột cho nhất quán
    private String dinhKemTenGoc;

    // Loại MIME của tệp đính kèm (ví dụ: image/jpeg, application/pdf)
    @Column(name = "dinh_kem_loai", length = 100)
    private String dinhKemLoai;

    // Thời gian gửi, tự động gán khi tạo mới
    @CreationTimestamp
    @Column(name = "thoi_gian_gui", nullable = false, updatable = false)
    private Timestamp thoiGianGui;

    // Cờ xóa mềm, mặc định là false
    @Builder.Default
    @Column(name = "soft_deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean softDeleted = false;
}