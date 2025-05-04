package com.hau.websocket.repository;

import com.hau.websocket.entity.PhongBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PhongBanRepository extends JpaRepository<PhongBan, Integer>, JpaSpecificationExecutor<PhongBan> {
}
