package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.KhoaResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.entity.Khoa;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.KhoaMapper;
import com.hau.websocket.repository.KhoaRepository;
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
public class KhoaService {
    private final KhoaRepository khoaRepository;
    private final KhoaMapper khoaMapper;

    public ApiResponse<PageResponse<KhoaResponse>> getAll(
            int page,
            int size,
            String maKhoa,
            String tenKhoa
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<Khoa> spec = buildSpecification(
                maKhoa,
                tenKhoa
        );

        Page<Khoa> khoaPage = khoaRepository.findAll(spec, pageable);

        List<KhoaResponse> khoaResponses = khoaPage.map(khoaMapper::toKhoaResponse).toList();

        return ApiResponse.<PageResponse<KhoaResponse>>builder()
                .status(200)
                .message("Lấy danh sách khoa thành công")
                .result(PageResponse.<KhoaResponse>builder()
                        .currentPage(page)
                        .totalPages(khoaPage.getTotalPages())
                        .totalElements(khoaPage.getTotalElements())
                        .pageSize(khoaPage.getSize())
                        .data(khoaResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<KhoaResponse> getOne(Integer id) {
        Khoa khoa = khoaRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Khoa không tồn tại", null));

        KhoaResponse khoaResponse = khoaMapper.toKhoaResponse(khoa);

        return ApiResponse.<KhoaResponse>builder()
                .status(200)
                .message("Lấy thông tin khoa thành công")
                .result(khoaResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<Khoa> buildSpecification(
            String maKhoa,
            String tenKhoa) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(maKhoa)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ma -> predicates.add(cb.like(cb.lower(root.get("maKhoa")), "%" + ma.toLowerCase() + "%")));

            Optional.ofNullable(tenKhoa)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("tenKhoa")), "%" + ten.toLowerCase() + "%")));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}