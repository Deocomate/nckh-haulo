package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.NhomResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.entity.Nhom;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.NhomMapper;
import com.hau.websocket.repository.NhomRepository;
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
public class NhomService {
    private final NhomRepository nhomRepository;
    private final NhomMapper nhomMapper;

    public ApiResponse<PageResponse<NhomResponse>> getAll(
            int page,
            int size,
            String tenNhom,
            String loaiNhom,
            Integer nguoiTaoId
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<Nhom> spec = buildSpecification(
                tenNhom,
                loaiNhom,
                nguoiTaoId
        );

        Page<Nhom> nhomPage = nhomRepository.findAll(spec, pageable);

        List<NhomResponse> nhomResponses = nhomPage.map(nhomMapper::toNhomResponse).toList();

        return ApiResponse.<PageResponse<NhomResponse>>builder()
                .status(200)
                .message("Lấy danh sách nhóm thành công")
                .result(PageResponse.<NhomResponse>builder()
                        .currentPage(page)
                        .totalPages(nhomPage.getTotalPages())
                        .totalElements(nhomPage.getTotalElements())
                        .pageSize(nhomPage.getSize())
                        .data(nhomResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<NhomResponse> getOne(Integer id) {
        Nhom nhom = nhomRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Nhóm không tồn tại", null));

        NhomResponse nhomResponse = nhomMapper.toNhomResponse(nhom);

        return ApiResponse.<NhomResponse>builder()
                .status(200)
                .message("Lấy thông tin nhóm thành công")
                .result(nhomResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<Nhom> buildSpecification(
            String tenNhom,
            String loaiNhom,
            Integer nguoiTaoId) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(tenNhom)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("tenNhom")), "%" + ten.toLowerCase() + "%")));

            Optional.ofNullable(loaiNhom)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(loai -> predicates.add(cb.equal(root.get("loaiNhom"), loai)));

            Optional.ofNullable(nguoiTaoId)
                    .ifPresent(id -> predicates.add(cb.equal(root.get("nguoiTaoId"), id)));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}