package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.LopHocResponse;
import com.hau.websocket.entity.LopHoc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LopHocMapper {
    LopHocResponse toLopHocResponse(LopHoc lopHoc);
}