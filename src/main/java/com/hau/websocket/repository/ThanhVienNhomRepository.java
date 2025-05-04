package com.hau.websocket.repository;

import com.hau.websocket.entity.ThanhVienNhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThanhVienNhomRepository extends JpaRepository<ThanhVienNhom, Integer>, JpaSpecificationExecutor<ThanhVienNhom> {
    Optional<ThanhVienNhom> findThanhVienNhomByNhomIdAndNguoiDungId(Integer nguoiDungId, Integer nhomId);
}
