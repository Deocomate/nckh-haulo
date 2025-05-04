package com.hau.websocket.mapper;

import com.hau.websocket.dto.response.SinhVienResponse;
import com.hau.websocket.entity.SinhVien;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SinhVienMapper {
    SinhVienResponse toSinhVienResponse(SinhVien sinhVien);
}