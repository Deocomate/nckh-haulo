package com.hau.websocket.repository;

import com.hau.websocket.entity.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaRepository extends JpaRepository<Khoa, Integer>, JpaSpecificationExecutor<Khoa> {
}
