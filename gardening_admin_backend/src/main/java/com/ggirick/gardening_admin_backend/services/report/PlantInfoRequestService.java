package com.ggirick.gardening_admin_backend.services.report;


import com.ggirick.gardening_admin_backend.dto.report.*;
import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;
import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequestFile;
import com.ggirick.gardening_admin_backend.repository.PlantInfoRequestFileRepository;
import com.ggirick.gardening_admin_backend.repository.PlantInfoRequestRepository;
import com.ggirick.gardening_admin_backend.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlantInfoRequestService {
    private final PlantInfoRequestRepository repository;
    private final PlantInfoRequestFileRepository fileRepo;
    private final FileUtil fileUtil;


    public PlantInfoRequestResponse toResponse(PlantInfoRequest entity) {
        return PlantInfoRequestResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .scientificName(entity.getScientificName())
                .changes(entity.getChanges())
                .reviewerUid(entity.getReviewerUid())
                .reviewNote(entity.getReviewNote())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .userUid(entity.getUserUid())
                .build();
    }

    public PlantInfoRequest updateEntity(PlantInfoRequest entity, PlantInfoRequestUpdateRequest dto, String uid) {
        entity.setStatus(dto.getStatus());
        entity.setId(entity.getId());
        entity.setReviewerUid(uid);
        entity.setReviewNote(dto.getReviewNote());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public PlantInfoRequestResponse createRequest(PlantInfoRequestCreateRequest dto,
                                                  List<MultipartFile> files,
                                                  String userUid) throws Exception {
        PlantInfoRequest req = PlantInfoRequest.builder()
                .scientificName(dto.getScientificName())
                .changes(dto.getChanges())
                .userUid(userUid)
                .status("pending")
                .build();
        PlantInfoRequest entity =  repository.save(req);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {

                Map<String, String> info =
                        fileUtil.uploadFileAndGetInfo(file.getOriginalFilename(),
                                "plant-info-request/" + entity.getId() + "/",
                                file);

                PlantInfoRequestFile fileEntity = PlantInfoRequestFile.builder()
                        .request(entity)
                        .oriName(info.get("oriName"))
                        .sysName(info.get("sysName"))
                        .url(info.get("url"))
                        .requestUserUid(userUid)
                        .build();

                fileRepo.save(fileEntity);
            }
        }

        return this.toResponse(entity);
    }

    public PlantInfoRequestResponse get(Long id) {
        PlantInfoRequest entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 요청이 존재하지 않습니다."));

        PlantInfoRequestResponse response =  this.toResponse(entity);

        List<PlantInfoRequestFile> requestFile = fileRepo.findByRequestId(id);

        response.setFiles(requestFile);
        return response;
    }

    public PlantInfoRequestResponse update(Long id, PlantInfoRequestUpdateRequest request, String uid) {
        PlantInfoRequest entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 요청이 존재하지 않습니다."));
        repository.save(this.updateEntity(entity, request,uid));

        return this.toResponse(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("이미 삭제되었거나 존재하지 않는 ID입니다.");
        }
        repository.deleteById(id);
    }

    public Page<PlantInfoRequestResponse> getAllWithFilter(
            String status,
            int start,
            int end,
            String sortField,
            String sortOrder
    ) {

        int size = end - start + 1;

        List<PlantInfoRequest> list = repository.findAllWithPagingAndFilter(
                status,
                sortField,
                sortOrder,
                start,
                end
        );

        long total = repository.countFiltered(status);

        List<PlantInfoRequestResponse> response = list.stream()
                .map(this::toResponse)
                .toList();

        // 페이지 번호 계산 (react-admin은 startIndex 기반)
        int page = start / size;

        return new PageImpl<>(response, PageRequest.of(page, size), total);
    }

}
