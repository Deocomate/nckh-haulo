package com.hau.websocket.repository;

import com.hau.websocket.entity.TinNhanLop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TinNhanLopRepository extends JpaRepository<TinNhanLop, Integer> {
    Page<TinNhanLop> findByLopHocId(Pageable pageable, Integer lopHocId);

    @Transactional
    void deleteByLopHocId(Integer lopHocId);
}