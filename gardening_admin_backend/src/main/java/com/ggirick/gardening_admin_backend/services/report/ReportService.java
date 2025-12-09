package com.ggirick.gardening_admin_backend.services.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.report.ReportCreateRequest;
import com.ggirick.gardening_admin_backend.dto.report.ReportResponse;
import com.ggirick.gardening_admin_backend.dto.report.ReportUpdateRequest;
import com.ggirick.gardening_admin_backend.entity.report.Report;
import com.ggirick.gardening_admin_backend.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    private final ObjectMapper objectMapper;

    public ReportResponse toResponse(Report entity) {
        return ReportResponse.builder()
                .id(entity.getId())
                .targetId(entity.getTargetId())
                .targetType(entity.getTargetType())
                .reporterUid(entity.getReporterUid())
                .status(entity.getStatus())
                .reason(entity.getReason())
                .build();
    }

    public ReportResponse createReport(ReportCreateRequest dto, String reporterUid) {
        Report report = Report.builder()
                .reason(dto.getReason())
                .targetId(dto.getTargetId())
                .targetType(dto.getTargetType())
                .reporterUid(reporterUid)
                .status("pending")
                .build();

        Report entity =  reportRepository.save(report);
        return toResponse(entity);
    }

    public ReportResponse get(Long id) {
        Report report =  reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 신고를 찾을 수 없습니다."));

       return toResponse(report);
    }

    public Page<ReportResponse> getAllWithFilter(
            String status,
            int start,
            int end,
            String sortField,
            String sortOrder
    ) {

        int size = end - start + 1;

        List<Report> list = reportRepository.findAllWithPagingAndFilter(status, sortField, sortOrder, start, end);
        long total = reportRepository.countFiltered(status);

        List<ReportResponse> response = list.stream()
                .map(this::toResponse)
                .toList();

        int page = start / size;

        return new PageImpl<>(response,  PageRequest.of(page, size), total);
    }
    public ReportResponse update(Long id, ReportUpdateRequest dto) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 신고를 찾을 수 없습니다."));

        report.setStatus(dto.getStatus());

        if (dto.getReason() != null) {
            report.setReason(dto.getReason());
        }

        Report entity =  reportRepository.save(report);

        return toResponse(entity);
    }


    public void delete(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("해당 신고를 찾을 수 없습니다.");
        }
        reportRepository.deleteById(id);
    }


    public List<Long> deleteMany(String filter) {
        return null;
    }

    public List<ReportResponse> updateMany(String filter, ReportUpdateRequest request) {
        return null;
    }

    public List<ReportResponse> getMany(String rawFilter) {
        return null;
    }


}
