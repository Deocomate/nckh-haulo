package com.hau.websocket.service;

import com.hau.websocket.dto.request.TinNhanCreateRequest;
import com.hau.websocket.dto.response.ApiResponse;
import com.hau.websocket.dto.response.PageResponse;
import com.hau.websocket.dto.response.TinNhanResponse;
import com.hau.websocket.entity.TinNhan;
import com.hau.websocket.exception.AppException;
import com.hau.websocket.mapper.TinNhanMapper;
import com.hau.websocket.repository.TinNhanRepository;
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
public class TinNhanService {
    private final TinNhanRepository tinNhanRepository;
    private final TinNhanMapper tinNhanMapper;
    private final FileService fileService;
    private final NguoiDungService nguoiDungService;

    @Value("${app.file.download-prefix}")
    private String fileDownloadPrefix;

    public ApiResponse<TinNhanResponse> crateTinNhan(TinNhanCreateRequest tinNhanCreateRequest, MultipartFile multipartFile) throws IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer id = Integer.valueOf(authentication.getName());

        TinNhan tinNhan = tinNhanMapper.toTinNhan(tinNhanCreateRequest);
        tinNhan.setNguoiGuiId(id);
        tinNhan.setCuocTroChuyenId(nguoiDungService.findUserById(id).getMaDangNhap() + "_" + nguoiDungService.findUserById(tinNhanCreateRequest.getNguoiNhanId()).getMaDangNhap());
        if (multipartFile != null && !multipartFile.isEmpty()) {
            var fileResponse = fileService.uploadFile(multipartFile);
            tinNhan.setDinhKemUrl(fileResponse.getUrl());
            tinNhan.setDinhKemTenGoc(fileResponse.getOriginalFileName());
            tinNhan.setDinhKemLoai(fileResponse.getContentType());
        }

        tinNhanRepository.save(tinNhan);
        TinNhanResponse tinNhanResponse = tinNhanMapper.toTinNhanResponse(tinNhan);
        if (tinNhanResponse.getDinhKemUrl() != null && !tinNhanResponse.getDinhKemUrl().isEmpty()) {
            tinNhanResponse.setDinhKemUrl(fileDownloadPrefix + tinNhanResponse.getDinhKemUrl());
        }
        return ApiResponse.<TinNhanResponse>builder()
                .status(200)
                .message("Đã gửi tin nhắn")
                .result(tinNhanResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<PageResponse<TinNhanResponse>> getTinNhanByCuocTroChuyenId(int page, int size, String cuocTroChuyenId) {
        check(cuocTroChuyenId);
        Sort sort = Sort.by("thoiGianGui").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<TinNhan> tinNhans = tinNhanRepository.findTinNhanByCuocTroChuyenId(pageable, cuocTroChuyenId);

        List<TinNhanResponse> tinNhanResponseList = tinNhans.map(tinNhanMapper::toTinNhanResponse).toList();
        tinNhanResponseList.forEach(tinNhanResponse -> tinNhanResponse.setDinhKemUrl(fileDownloadPrefix + tinNhanResponse.getDinhKemUrl()));
        return ApiResponse.<PageResponse<TinNhanResponse>>builder()
                .status(200)
                .message("Lấy thông tin thành công")
                .result(
                        PageResponse.<TinNhanResponse>builder()
                                .currentPage(page)
                                .totalPages(tinNhans.getTotalPages())
                                .totalElements(tinNhans.getTotalElements())
                                .pageSize(tinNhans.getSize())
                                .data(tinNhanResponseList)
                                .build()
                )
                .timestamp(LocalDateTime.now())
                .build();
    }


    public void deleteTinNhan(Integer id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userId = Integer.valueOf(authentication);
        TinNhan tinNhan = tinNhanRepository.findById(id).orElseThrow(() -> new RuntimeException("Tin nhan khong ton tai"));
        if (!tinNhan.getNguoiGuiId().equals(userId)) {
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa tin nhắn này", null);
        }
        tinNhanRepository.delete(tinNhan);
    }


    public void check(String cuocTroChuyenId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        String currentUsername = nguoiDungService.findUserById(userId).getMaDangNhap();
        String[] participants = cuocTroChuyenId.split("_");
        if (participants.length != 2) {
            // cuocTroChuyenId không đúng định dạng
            throw new AppException(HttpStatus.BAD_REQUEST, "Định dạng cuocTroChuyenId không hợp lệ.", null);
        }
        String participant1 = participants[0];
        String participant2 = participants[1];

        if (!currentUsername.equals(participant1) && !currentUsername.equals(participant2)) {
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào cuộc trò chuyện này.", null);
        }
    }

}
