package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.SinhVienResponse;
import com.hau.websocket.entity.SinhVien;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.SinhVienMapper;
import com.hau.websocket.repository.SinhVienRepository;
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
public class SinhVienService {
    private final SinhVienRepository sinhVienRepository;
    private final SinhVienMapper sinhVienMapper;

    public ApiResponse<PageResponse<SinhVienResponse>> getAll(
            int page,
            int size,
            String maSinhVien,
            Integer nguoiDungId,
            Integer lopHocId
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<SinhVien> spec = buildSpecification(
                maSinhVien,
                nguoiDungId,
                lopHocId
        );

        Page<SinhVien> sinhVienPage = sinhVienRepository.findAll(spec, pageable);

        List<SinhVienResponse> sinhVienResponses = sinhVienPage.map(sinhVienMapper::toSinhVienResponse).toList();

        return ApiResponse.<PageResponse<SinhVienResponse>>builder()
                .status(200)
                .message("Lấy danh sách sinh viên thành công")
                .result(PageResponse.<SinhVienResponse>builder()
                        .currentPage(page)
                        .totalPages(sinhVienPage.getTotalPages())
                        .totalElements(sinhVienPage.getTotalElements())
                        .pageSize(sinhVienPage.getSize())
                        .data(sinhVienResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<SinhVienResponse> getOne(Integer id) {
        SinhVien sinhVien = sinhVienRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Sinh viên không tồn tại", null));

        SinhVienResponse sinhVienResponse = sinhVienMapper.toSinhVienResponse(sinhVien);

        return ApiResponse.<SinhVienResponse>builder()
                .status(200)
                .message("Lấy thông tin sinh viên thành công")
                .result(sinhVienResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<SinhVien> buildSpecification(
            String maSinhVien,
            Integer nguoiDungId,
            Integer lopHocId) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(maSinhVien)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ma -> predicates.add(cb.like(cb.lower(root.get("maSinhVien")), "%" + ma.toLowerCase() + "%")));

            Optional.ofNullable(nguoiDungId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("nguoiDungId"), id)));

            Optional.ofNullable(lopHocId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("lopHocId"), id)));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}