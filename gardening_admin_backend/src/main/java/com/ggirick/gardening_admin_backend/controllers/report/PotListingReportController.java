package com.ggirick.gardening_admin_backend.controllers.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.auth.UserTokenDTO;
import com.ggirick.gardening_admin_backend.dto.report.PotListingReportCreateRequest;
import com.ggirick.gardening_admin_backend.dto.report.PotListingReportResponse;
import com.ggirick.gardening_admin_backend.dto.report.PotListingReportUpdateRequest;
import com.ggirick.gardening_admin_backend.entity.report.PotListingReport;
import com.ggirick.gardening_admin_backend.services.report.PotListingReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/potlistingreport")
@RequiredArgsConstructor
public class PotListingReportController {

    private final PotListingReportService service;
    private final ObjectMapper mapper;

    @PostMapping
    public ResponseEntity<PotListingReport> createReport(
            @RequestBody PotListingReportCreateRequest request,
            @AuthenticationPrincipal UserTokenDTO userInfo
    ) {
        return ResponseEntity.ok(
                service.createReport(request, userInfo.getUid())
        );
    }


    @GetMapping("/{id}")
    public PotListingReportResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // 🔥 List + Pagination + Sorting + Filtering (react-admin 호환)
    @GetMapping
    public ResponseEntity<List<PotListingReportResponse>> getAll(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String filter,
            HttpServletResponse response
    ) throws JsonProcessingException {

        JsonNode sortNode = mapper.readTree(sort);
        String sortField = sortNode.get("field").asText();
        String sortOrder = sortNode.get("order").asText();

        JsonNode rangeNode = mapper.readTree(range);
        int start = rangeNode.get(0).asInt();
        int end = rangeNode.get(1).asInt();

        JsonNode filterNode = mapper.readTree(filter);
        String status = filterNode.has("status") ? filterNode.get("status").asText() : null;

        Page<PotListingReportResponse> page =
                service.getAllWithFilter(status, start, end, sortField, sortOrder);

        long total = page.getTotalElements();
        List<PotListingReportResponse> content = page.getContent();

        response.setHeader(
                "Content-Range",
                String.format("potlistingreport %d-%d/%d",
                        start,
                        start + content.size() - 1,
                        total
                )
        );
        response.setHeader("Access-Control-Expose-Headers", "Content-Range");

        return ResponseEntity.ok(content);
    }


    @PutMapping("/{id}")
    public PotListingReportResponse update(
            @PathVariable Long id,
            @RequestBody PotListingReportUpdateRequest request,
            @AuthenticationPrincipal UserTokenDTO userInfo
    ) {
        return service.update(id, request, userInfo.getUid());
    }

    // 🔥 Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
