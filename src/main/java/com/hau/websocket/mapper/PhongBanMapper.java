package com.hau.websocket.mapper;

import com.hau.websocket.entity.PhongBan;
import com.hau.websocket.dto.response.PhongBanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhongBanMapper {
    PhongBanResponse toPhongBanResponse(PhongBan phongBan);
}
