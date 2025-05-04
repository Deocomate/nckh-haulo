package com.hau.websocket.repository;

import com.hau.websocket.entity.Nhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomRepository extends JpaRepository<Nhom, Integer>, JpaSpecificationExecutor<Nhom> {
}
