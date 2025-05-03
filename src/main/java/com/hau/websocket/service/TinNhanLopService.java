package com.hau.websocket.service;

import com.hau.websocket.dto.request.TinNhanLopCreateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.TinNhanLopResponse;
import com.hau.websocket.entity.TinNhanLop;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.TinNhanLopMapper;
import com.hau.websocket.repository.TinNhanLopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinNhanLopService {
    private final TinNhanLopRepository tinNhanLopRepository;
    private final TinNhanLopMapper tinNhanLopMapper;
    private final FileService fileService;

    @Value("${app.file.download-prefix}")
    private String fileDownloadPrefix;

    public void createTinNhanLop(TinNhanLopCreateRequest tinNhanLopCreateRequest, MultipartFile multipartFile) throws IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer nguoiGuiId = Integer.valueOf(authentication.getName());

        TinNhanLop tinNhanLop = tinNhanLopMapper.toTinNhanLop(tinNhanLopCreateRequest);
        tinNhanLop.setNguoiGuiId(nguoiGuiId);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            var fileResponse = fileService.uploadFile(multipartFile);
            tinNhanLop.setDinhKemUrl(fileResponse.getUrl());
            tinNhanLop.setDinhKemTenGoc(fileResponse.getOriginalFileName());
            tinNhanLop.setDinhKemLoai(fileResponse.getContentType());
        }

        tinNhanLopRepository.save(tinNhanLop);
    }

    public ApiResponse<PageResponse<TinNhanLopResponse>> getTinNhanLopByLopHocId(int pageIndex, int pageSize, Integer lopHocId) {
        Sort sort = Sort.by("thoiGianGui").descending();

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, sort);
        Page<TinNhanLop> tinNhans = tinNhanLopRepository.findByLopHocId(pageable, lopHocId);

        List<TinNhanLopResponse> tinNhanResponseList = tinNhans.map(tinNhanLopMapper::toTinNhanLopResponse).toList();
        tinNhanResponseList.forEach(msg -> {
            if (msg.getDinhKemUrl() != null && !msg.getDinhKemUrl().isEmpty()) {
                msg.setDinhKemUrl(fileDownloadPrefix + msg.getDinhKemUrl());
            }
        });

        return ApiResponse.<PageResponse<TinNhanLopResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách tin nhắn lớp học thành công")
                .result(
                        PageResponse.<TinNhanLopResponse>builder()
                                .currentPage(pageIndex)
                                .totalPages(tinNhans.getTotalPages())
                                .totalElements(tinNhans.getTotalElements())
                                .pageSize(tinNhans.getSize())
                                .data(tinNhanResponseList)
                                .build()
                )
                .timestamp(LocalDateTime.now())
                .build();
    }

    public void deleteTinNhanLop(Integer id) {
        if (!tinNhanLopRepository.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Tin nhắn lớp học không tồn tại", null);
        }
        tinNhanLopRepository.deleteById(id);
    }

    public void deleteAllTinNhanLopByLopHocId(Integer lopHocId) {
        tinNhanLopRepository.deleteByLopHocId(lopHocId);
    }
}