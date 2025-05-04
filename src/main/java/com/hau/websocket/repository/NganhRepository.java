package com.hau.websocket.repository;

import com.hau.websocket.entity.Nganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NganhRepository extends JpaRepository<Nganh, Integer>, JpaSpecificationExecutor<Nganh> {
}
