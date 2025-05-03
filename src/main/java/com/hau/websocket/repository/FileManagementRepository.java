package com.hau.websocket.repository;

import com.hau.websocket.entity.FileManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagementRepository extends JpaRepository<FileManagement, String> {

}
