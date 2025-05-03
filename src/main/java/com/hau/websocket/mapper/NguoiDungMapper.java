package com.hau.websocket.mapper;

import com.hau.websocket.dto.request.NguoiDungCreateRequest;
import com.hau.websocket.dto.request.NguoiDungUpdateRequest;
import com.hau.websocket.dto.response.NguoiDungResponse;
import com.hau.websocket.entity.NguoiDung;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NguoiDungMapper {
    NguoiDung toNguoiDung(NguoiDungCreateRequest nguoiDungCreateRequest);

    void toNguoiDungUpdateRequest(@MappingTarget NguoiDung nguoiDung, NguoiDungUpdateRequest nguoiDungUpdateRequest);

    NguoiDungResponse toNguoiDungResponse(NguoiDung nguoiDung);
}
