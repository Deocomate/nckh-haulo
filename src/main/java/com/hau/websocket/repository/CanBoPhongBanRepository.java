package com.hau.websocket.repository;

import com.hau.websocket.entity.CanBoPhongBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CanBoPhongBanRepository extends JpaRepository<CanBoPhongBan, Integer>, JpaSpecificationExecutor<CanBoPhongBan> {
}
