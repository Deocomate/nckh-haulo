package com.hau.websocket.repository;

import com.hau.websocket.entity.TinNhan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TinNhanRepository extends JpaRepository<TinNhan, Integer> {
    Page<TinNhan> findTinNhanByCuocTroChuyenId(Pageable pageable, Integer maCuocTroChuyenId);

    @Transactional
    void deleteByCuocTroChuyenId(Integer cuocTroChuyenId);
}
