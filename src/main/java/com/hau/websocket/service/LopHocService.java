package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.LopHocResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.entity.LopHoc;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.LopHocMapper;
import com.hau.websocket.repository.LopHocRepository;
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
public class LopHocService {
    private final LopHocRepository lopHocRepository;
    private final LopHocMapper lopHocMapper;

    public ApiResponse<PageResponse<LopHocResponse>> getAll(
            int page,
            int size,
            String maLop,
            String tenLop,
            String nienKhoa,
            Integer nganhId
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<LopHoc> spec = buildSpecification(
                maLop,
                tenLop,
                nienKhoa,
                nganhId
        );

        Page<LopHoc> lopHocPage = lopHocRepository.findAll(spec, pageable);

        List<LopHocResponse> lopHocResponses = lopHocPage.map(lopHocMapper::toLopHocResponse).toList();

        return ApiResponse.<PageResponse<LopHocResponse>>builder()
                .status(200)
                .message("Lấy danh sách lớp học thành công")
                .result(PageResponse.<LopHocResponse>builder()
                        .currentPage(page)
                        .totalPages(lopHocPage.getTotalPages())
                        .totalElements(lopHocPage.getTotalElements())
                        .pageSize(lopHocPage.getSize())
                        .data(lopHocResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<LopHocResponse> getOne(Integer id) {
        LopHoc lopHoc = lopHocRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Lớp học không tồn tại", null));

        LopHocResponse lopHocResponse = lopHocMapper.toLopHocResponse(lopHoc);

        return ApiResponse.<LopHocResponse>builder()
                .status(200)
                .message("Lấy thông tin lớp học thành công")
                .result(lopHocResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<LopHoc> buildSpecification(
            String maLop,
            String tenLop,
            String nienKhoa,
            Integer nganhId) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(maLop)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ma -> predicates.add(cb.like(cb.lower(root.get("maLop")), "%" + ma.toLowerCase() + "%")));

            Optional.ofNullable(tenLop)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("tenLop")), "%" + ten.toLowerCase() + "%")));

            Optional.ofNullable(nienKhoa)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(nien -> predicates.add(cb.like(cb.lower(root.get("nienKhoa")), "%" + nien.toLowerCase() + "%")));

            Optional.ofNullable(nganhId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("nganhId"), id)));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}