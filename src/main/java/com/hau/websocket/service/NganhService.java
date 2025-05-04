package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.NganhResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.entity.Nganh;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.NganhMapper;
import com.hau.websocket.repository.NganhRepository;
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
public class NganhService {
    private final NganhRepository nganhRepository;
    private final NganhMapper nganhMapper;

    public ApiResponse<PageResponse<NganhResponse>> getAll(
            int page,
            int size,
            String maNganh,
            String tenNganh,
            String tenKhoa // Changed from Integer khoaId
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<Nganh> spec = buildSpecification(
                maNganh,
                tenNganh,
                tenKhoa // Changed from khoaId
        );

        Page<Nganh> nganhPage = nganhRepository.findAll(spec, pageable);

        List<NganhResponse> nganhResponses = nganhPage.map(nganhMapper::toNganhResponse).toList();

        return ApiResponse.<PageResponse<NganhResponse>>builder()
                .status(200)
                .message("Lấy danh sách ngành thành công")
                .result(PageResponse.<NganhResponse>builder()
                        .currentPage(page)
                        .totalPages(nganhPage.getTotalPages())
                        .totalElements(nganhPage.getTotalElements())
                        .pageSize(nganhPage.getSize())
                        .data(nganhResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<NganhResponse> getOne(Integer id) {
        Nganh nganh = nganhRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Ngành không tồn tại", null));

        NganhResponse nganhResponse = nganhMapper.toNganhResponse(nganh);

        return ApiResponse.<NganhResponse>builder()
                .status(200)
                .message("Lấy thông tin ngành thành công")
                .result(nganhResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<Nganh> buildSpecification(
            String maNganh,
            String tenNganh,
            String tenKhoa) { // Changed from Integer khoaId

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(maNganh)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ma -> predicates.add(cb.like(cb.lower(root.get("maNganh")), "%" + ma.toLowerCase() + "%")));

            Optional.ofNullable(tenNganh)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("tenNganh")), "%" + ten.toLowerCase() + "%")));


            Optional.ofNullable(tenKhoa)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("khoa").get("tenKhoa")), "%" + ten.toLowerCase() + "%")));


            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}