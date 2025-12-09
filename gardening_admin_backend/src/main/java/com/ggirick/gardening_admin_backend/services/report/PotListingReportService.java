package com.ggirick.gardening_admin_backend.services.report;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.report.PotListingReportCreateRequest;
import com.ggirick.gardening_admin_backend.dto.report.PotListingReportResponse;
import com.ggirick.gardening_admin_backend.dto.report.PotListingReportUpdateRequest;
import com.ggirick.gardening_admin_backend.entity.report.PotListingReport;
import com.ggirick.gardening_admin_backend.repository.PotListingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PotListingReportService {
    private final PotListingReportRepository repository;

    public PotListingReportResponse toResponse(PotListingReport entity) {
        return PotListingReportResponse.builder()
                .id(entity.getId())
                .potListingId(entity.getPotListingId())
                .reason(entity.getReason())
                .reporterUid(entity.getReporterUid())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public PotListingReport createReport(PotListingReportCreateRequest dto, String reporterUid) {
        PotListingReport report = PotListingReport.builder()
                .potListingId(dto.getPotListingId())
                .reason(dto.getReason())
                .reporterUid(reporterUid)
                .status("pending")
                .build();

        return repository.save(report);
    }

    public PotListingReportResponse get(Long id) {
        PotListingReport report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 신고를 찾을 수 없습니다."));

        return toResponse(report);
    }


    public Page<PotListingReportResponse> getAllWithFilter(
            String status,
            int start,
            int end,
            String sortField,
            String sortOrder
    ) {
        int size = end - start + 1;
        int page = start / size;

        List<PotListingReport> list =
                repository.findAllWithPagingAndFilter(status, sortField, sortOrder, start, end);

        long total = repository.countFiltered(status);

        List<PotListingReportResponse> mapped = list.stream()
                .map(this::toResponse)
                .toList();

        return new PageImpl<>(mapped, PageRequest.of(page, size), total);
    }


    public PotListingReportResponse update(Long id, PotListingReportUpdateRequest request, String uid) {
        PotListingReport entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 신고를 찾을 수 없습니다."));

        entity.setStatus(request.getStatus());

        PotListingReport saved = repository.save(entity);
        return toResponse(saved);
    }


    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("해당 신고를 찾을 수 없습니다.");
        }
        repository.deleteById(id);
    }
}
