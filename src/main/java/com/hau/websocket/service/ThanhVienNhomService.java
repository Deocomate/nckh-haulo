package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.ThanhVienNhomResponse;
import com.hau.websocket.entity.ThanhVienNhom;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.ThanhVienNhomMapper;
import com.hau.websocket.repository.ThanhVienNhomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThanhVienNhomService {
    private final ThanhVienNhomRepository thanhVienNhomRepository;
    private final ThanhVienNhomMapper thanhVienNhomMapper;

    public ApiResponse<PageResponse<ThanhVienNhomResponse>> getAll(
            int page,
            int size,
            Integer nhomId,
            Integer nguoiDungId,
            String vaiTroTrongNhom
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayThamGia").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<ThanhVienNhom> spec = buildSpecification(
                nhomId,
                nguoiDungId,
                vaiTroTrongNhom
        );

        Page<ThanhVienNhom> thanhVienNhomPage = thanhVienNhomRepository.findAll(spec, pageable);

        List<ThanhVienNhomResponse> thanhVienNhomResponses = thanhVienNhomPage.map(thanhVienNhomMapper::toThanhVienNhomResponse).toList();

        return ApiResponse.<PageResponse<ThanhVienNhomResponse>>builder()
                .status(200)
                .message("Lấy danh sách thành viên nhóm thành công")
                .result(PageResponse.<ThanhVienNhomResponse>builder()
                        .currentPage(page)
                        .totalPages(thanhVienNhomPage.getTotalPages())
                        .totalElements(thanhVienNhomPage.getTotalElements())
                        .pageSize(thanhVienNhomPage.getSize())
                        .data(thanhVienNhomResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<ThanhVienNhomResponse> getOne(Integer id) {
        ThanhVienNhom thanhVienNhom = thanhVienNhomRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Thành viên nhóm không tồn tại", null));

        ThanhVienNhomResponse thanhVienNhomResponse = thanhVienNhomMapper.toThanhVienNhomResponse(thanhVienNhom);

        return ApiResponse.<ThanhVienNhomResponse>builder()
                .status(200)
                .message("Lấy thông tin thành viên nhóm thành công")
                .result(thanhVienNhomResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<ThanhVienNhom> buildSpecification(
            Integer nhomId,
            Integer nguoiDungId,
            String vaiTroTrongNhom) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(nhomId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("nhomId"), id)));

            Optional.ofNullable(nguoiDungId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("nguoiDungId"), id)));

            Optional.ofNullable(vaiTroTrongNhom)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(vaiTro -> predicates.add(cb.equal(root.get("vaiTroTrongNhom"), vaiTro)));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}