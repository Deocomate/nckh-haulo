package com.hau.websocket.repository;

import com.hau.websocket.entity.TinNhanNhom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TinNhanNhomRepository extends JpaRepository<TinNhanNhom, Integer> {
    Page<TinNhanNhom> findByNhomId(Pageable pageable, Integer nhomId);

    @Transactional
    void deleteByNhomId(Integer nhomId);
}