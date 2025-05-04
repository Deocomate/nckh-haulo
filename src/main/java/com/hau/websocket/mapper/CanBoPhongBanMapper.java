package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.CanBoPhongBanResponse;
import com.hau.websocket.entity.CanBoPhongBan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CanBoPhongBanMapper {
    CanBoPhongBanResponse toCanBoPhongBanResponse(CanBoPhongBan canBoPhongBan);
}
