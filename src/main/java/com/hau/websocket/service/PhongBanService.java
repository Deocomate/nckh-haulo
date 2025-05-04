package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.PhongBanResponse;
import com.hau.websocket.entity.PhongBan;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.PhongBanMapper;
import com.hau.websocket.repository.PhongBanRepository;
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
public class PhongBanService {
    private final PhongBanRepository phongBanRepository;
    private final PhongBanMapper phongBanMapper;

    public ApiResponse<PageResponse<PhongBanResponse>> getAll(
            int page,
            int size,
            String maPhongBan,
            String tenPhongBan,
            String emailLienHe,
            String soDienThoaiLienHe
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<PhongBan> spec = buildSpecification(
                maPhongBan,
                tenPhongBan,
                emailLienHe,
                soDienThoaiLienHe
        );

        Page<PhongBan> phongBanPage = phongBanRepository.findAll(spec, pageable);

        List<PhongBanResponse> phongBanResponses = phongBanPage.map(phongBanMapper::toPhongBanResponse).toList();

        return ApiResponse.<PageResponse<PhongBanResponse>>builder()
                .status(200)
                .message("Lấy danh sách phòng ban thành công")
                .result(PageResponse.<PhongBanResponse>builder()
                        .currentPage(page)
                        .totalPages(phongBanPage.getTotalPages())
                        .totalElements(phongBanPage.getTotalElements())
                        .pageSize(phongBanPage.getSize())
                        .data(phongBanResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<PhongBanResponse> getOne(Integer id) {
        PhongBan phongBan = phongBanRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Phòng ban không tồn tại", null));

        PhongBanResponse phongBanResponse = phongBanMapper.toPhongBanResponse(phongBan);

        return ApiResponse.<PhongBanResponse>builder()
                .status(200)
                .message("Lấy thông tin phòng ban thành công")
                .result(phongBanResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<PhongBan> buildSpecification(
            String maPhongBan,
            String tenPhongBan,
            String emailLienHe,
            String soDienThoaiLienHe) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(maPhongBan)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ma -> predicates.add(cb.like(cb.lower(root.get("maPhongBan")), "%" + ma.toLowerCase() + "%")));

            Optional.ofNullable(tenPhongBan)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("tenPhongBan")), "%" + ten.toLowerCase() + "%")));

            Optional.ofNullable(emailLienHe)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(email -> predicates.add(cb.like(cb.lower(root.get("emailLienHe")), "%" + email.toLowerCase() + "%")));

            Optional.ofNullable(soDienThoaiLienHe)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(sdt -> predicates.add(cb.like(root.get("soDienThoaiLienHe"), "%" + sdt + "%")));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}