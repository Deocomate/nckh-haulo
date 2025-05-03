package com.hau.websocket.mapper;

import com.hau.websocket.dto.request.TinNhanNhomCreateRequest;
import com.hau.websocket.dto.response.TinNhanNhomResponse;
import com.hau.websocket.entity.TinNhanNhom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TinNhanNhomMapper {
    TinNhanNhom toTinNhanNhom(TinNhanNhomCreateRequest tinNhanNhomCreateRequest);

    TinNhanNhomResponse toTinNhanNhomResponse(TinNhanNhom tinNhanNhom);
}