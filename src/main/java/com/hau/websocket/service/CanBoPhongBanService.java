package com.hau.websocket.service;

import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.CanBoPhongBanResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.entity.CanBoPhongBan;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.CanBoPhongBanMapper;
import com.hau.websocket.repository.CanBoPhongBanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification; // Import Specification
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate; // Import Predicate

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Import Optional

@Service
@RequiredArgsConstructor
@Slf4j
public class CanBoPhongBanService {
    private final CanBoPhongBanRepository canBoPhongBanRepository;
    private final CanBoPhongBanMapper canBoPhongBanMapper;

    public ApiResponse<PageResponse<CanBoPhongBanResponse>> getAll(
            int page,
            int size,
            String tenPhongBan, // Changed from Integer phongBanId
            String vaiTroNhanVien,
            String maCanBo,
            Boolean laDauMoiLienLac
    ) {
        int adjustedPage = Math.max(0, page - 1);

        Sort sort = Sort.by("ngayTao").descending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);

        Specification<CanBoPhongBan> spec = buildSpecification(
                tenPhongBan, // Changed from phongBanId
                vaiTroNhanVien,
                maCanBo,
                laDauMoiLienLac
        );

        Page<CanBoPhongBan> canBoPhongBanPage = canBoPhongBanRepository.findAll(spec, pageable);

        List<CanBoPhongBanResponse> canBoPhongBanResponses = canBoPhongBanPage.map(canBoPhongBanMapper::toCanBoPhongBanResponse).toList();

        return ApiResponse.<PageResponse<CanBoPhongBanResponse>>builder()
                .status(200)
                .message("Lấy danh sách cán bộ phòng ban thành công")
                .result(PageResponse.<CanBoPhongBanResponse>builder()
                        .currentPage(page)
                        .totalPages(canBoPhongBanPage.getTotalPages())
                        .totalElements(canBoPhongBanPage.getTotalElements())
                        .pageSize(canBoPhongBanPage.getSize())
                        .data(canBoPhongBanResponses)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<CanBoPhongBanResponse> getOne(Integer id) {
        CanBoPhongBan canBoPhongBan = canBoPhongBanRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND,"Cán bộ phòng ban không tồn tại", null));

        CanBoPhongBanResponse canBoPhongBanResponse = canBoPhongBanMapper.toCanBoPhongBanResponse(canBoPhongBan);

        return ApiResponse.<CanBoPhongBanResponse>builder()
                .status(200)
                .message("Lấy thông tin cán bộ phòng ban thành công")
                .result(canBoPhongBanResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Specification<CanBoPhongBan> buildSpecification(
            String tenPhongBan, // Changed from Integer phongBanId
            String vaiTroNhanVien,
            String maCanBo,
            Boolean laDauMoiLienLac) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo tenPhongBan
            Optional.ofNullable(tenPhongBan)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ten -> predicates.add(cb.like(cb.lower(root.get("phongBan").get("tenPhongBan")), "%" + ten.toLowerCase() + "%")));


            // Lọc theo vaiTroNhanVien
            Optional.ofNullable(vaiTroNhanVien)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(vaiTro -> predicates.add(cb.equal(root.get("vaiTroNhanVien"), vaiTro)));


            // Lọc theo maCanBo
            Optional.ofNullable(maCanBo)
                    .filter(s -> !s.trim().isEmpty())
                    .ifPresent(ma -> predicates.add(cb.equal(root.get("maCanBo"), ma)));


            // Lọc theo laDauMoiLienLac
            Optional.ofNullable(laDauMoiLienLac)
                    .ifPresent(isDauMoi -> predicates.add(cb.equal(root.get("laDauMoiLienLac"), isDauMoi)));

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}