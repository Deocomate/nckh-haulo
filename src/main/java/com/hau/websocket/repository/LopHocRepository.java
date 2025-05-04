package com.hau.websocket.repository;

import com.hau.websocket.entity.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer>, JpaSpecificationExecutor<LopHoc> {
}
