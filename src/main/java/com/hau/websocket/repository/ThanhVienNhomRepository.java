package com.hau.websocket.repository;

import com.hau.websocket.entity.ThanhVienNhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThanhVienNhomRepository extends JpaRepository<ThanhVienNhom, Integer> {
    Optional<ThanhVienNhom> findByMaThanhVienAndMaNhom(Integer nguoiDungId, Integer nhomId);
}
