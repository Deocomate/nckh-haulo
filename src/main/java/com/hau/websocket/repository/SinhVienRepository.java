package com.hau.websocket.repository;

import com.hau.websocket.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Integer>, JpaSpecificationExecutor<SinhVien> {
}
