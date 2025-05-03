package com.hau.websocket.mapper;

import com.hau.websocket.dto.request.TinNhanLopCreateRequest;
import com.hau.websocket.dto.response.TinNhanLopResponse;
import com.hau.websocket.entity.TinNhanLop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TinNhanLopMapper {
    TinNhanLop toTinNhanLop(TinNhanLopCreateRequest tinNhanLopCreateRequest);

    TinNhanLopResponse toTinNhanLopResponse(TinNhanLop tinNhanLop);
}