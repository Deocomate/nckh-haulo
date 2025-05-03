package com.hau.websocket.service;

import com.hau.websocket.dto.request.TinNhanNhomCreateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.TinNhanNhomResponse;
import com.hau.websocket.entity.TinNhanNhom;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.TinNhanNhomMapper;
import com.hau.websocket.repository.TinNhanNhomRepository;
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
public class TinNhanNhomService {
    private final TinNhanNhomRepository tinNhanNhomRepository;
    private final TinNhanNhomMapper tinNhanNhomMapper;
    private final FileService fileService; // Assuming file service can handle files for any entity

    @Value("${app.file.download-prefix}")
    private String fileDownloadPrefix;

    public void createTinNhanNhom(TinNhanNhomCreateRequest tinNhanNhomCreateRequest, MultipartFile multipartFile) throws IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer nguoiGuiId = Integer.valueOf(authentication.getName());

        TinNhanNhom tinNhanNhom = tinNhanNhomMapper.toTinNhanNhom(tinNhanNhomCreateRequest);
        tinNhanNhom.setNguoiGuiId(nguoiGuiId);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            var fileResponse = fileService.uploadFile(multipartFile);
            tinNhanNhom.setDinhKemUrl(fileResponse.getUrl());
            tinNhanNhom.setDinhKemTenGoc(fileResponse.getOriginalFileName());
            tinNhanNhom.setDinhKemLoai(fileResponse.getContentType());
        }

        tinNhanNhomRepository.save(tinNhanNhom);
    }

    public ApiResponse<PageResponse<TinNhanNhomResponse>> getTinNhanNhomByNhomId(int pageIndex, int pageSize, Integer nhomId) {
        Sort sort = Sort.by("thoiGianGui").descending();

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, sort);
        Page<TinNhanNhom> tinNhans = tinNhanNhomRepository.findByNhomId(pageable, nhomId);

        List<TinNhanNhomResponse> tinNhanResponseList = tinNhans.map(tinNhanNhomMapper::toTinNhanNhomResponse).toList();
        tinNhanResponseList.forEach(msg -> {
            if (msg.getDinhKemUrl() != null && !msg.getDinhKemUrl().isEmpty()) {
                msg.setDinhKemUrl(fileDownloadPrefix + msg.getDinhKemUrl());
            }
        });

        return ApiResponse.<PageResponse<TinNhanNhomResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách tin nhắn nhóm thành công")
                .result(
                        PageResponse.<TinNhanNhomResponse>builder()
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


    public void deleteTinNhanNhom(Integer id) {
        if (!tinNhanNhomRepository.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Tin nhắn nhóm không tồn tại", null);
        }
        tinNhanNhomRepository.deleteById(id);
    }

    public void deleteAllTinNhanNhomByNhomId(Integer nhomId) {
        tinNhanNhomRepository.deleteByNhomId(nhomId);
    }
}