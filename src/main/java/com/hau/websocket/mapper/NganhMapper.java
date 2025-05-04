package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.NganhResponse;
import com.hau.websocket.entity.Nganh;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NganhMapper {
    NganhResponse toNganhResponse(Nganh nganh);
}