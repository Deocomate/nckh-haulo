package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.KhoaResponse;
import com.hau.websocket.entity.Khoa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KhoaMapper {
    KhoaResponse toKhoaResponse(Khoa khoa);
}