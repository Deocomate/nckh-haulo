package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.FileInfo;
import com.hau.websocket.entity.FileManagement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileManagementMapper {
    @Mapping(target = "id", source = "fileName")
    FileManagement toFileManagement(FileInfo fileInfo);

}
