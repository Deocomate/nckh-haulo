package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.ThanhVienNhomResponse;
import com.hau.websocket.entity.ThanhVienNhom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ThanhVienNhomMapper {
    ThanhVienNhomResponse toThanhVienNhomResponse(ThanhVienNhom thanhVienNhom);
}