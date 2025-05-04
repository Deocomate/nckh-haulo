package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.NhomResponse;
import com.hau.websocket.entity.Nhom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NhomMapper {
    NhomResponse toNhomResponse(Nhom nhom);
}