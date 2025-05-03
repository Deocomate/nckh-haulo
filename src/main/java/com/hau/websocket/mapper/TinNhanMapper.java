package com.hau.websocket.mapper;

import com.hau.websocket.dto.request.TinNhanCreateRequest;
import com.hau.websocket.dto.response.TinNhanResponse;
import com.hau.websocket.entity.TinNhan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TinNhanMapper {
    TinNhan toTinNhan(TinNhanCreateRequest tinNhanCreateRequest);

    TinNhanResponse toTinNhanResponse(TinNhan tinNhan);
}
