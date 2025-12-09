package com.ggirick.gardening_admin_backend.controllers.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.auth.UserTokenDTO;
import com.ggirick.gardening_admin_backend.dto.report.ReportCreateRequest;
import com.ggirick.gardening_admin_backend.dto.report.ReportResponse;
import com.ggirick.gardening_admin_backend.dto.report.ReportUpdateRequest;
import com.ggirick.gardening_admin_backend.services.report.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ObjectMapper mapper;

    @PostMapping
    public ReportResponse createReport(
            @RequestBody ReportCreateRequest request,
            @AuthenticationPrincipal UserTokenDTO userInfo
    ) {
       return reportService.createReport(request, userInfo.getUid());

    }

    @GetMapping("/{id}")
    public ReportResponse getReport(@PathVariable Long id){
        return reportService.get(id);
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAll(
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

        Page<ReportResponse> page = reportService.getAllWithFilter(status, start, end, sortField, sortOrder);

        long total = page.getTotalElements();
        List<ReportResponse> content = page.getContent();

        response.setHeader("Content-Range", "reports " + start + "-" + (start + content.size() - 1) + "/" + total);
        response.setHeader("Access-Control-Expose-Headers", "Content-Range");
        return ResponseEntity.ok()
                .body(content);
    }

    @PutMapping("/{id}")
    public ReportResponse updateReport(
            @PathVariable Long id, @RequestBody ReportUpdateRequest request){

        return reportService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id){
        reportService.delete(id);
    }

    @GetMapping("/batch")
    public List<ReportResponse> getMany(@RequestParam Map<String, String> params) {
        String rawFilter = params.get("filter");  // {"id":[1,2,3]}

        return reportService.getMany(rawFilter);
    }

    @PutMapping
    public List<ReportResponse> updateMany(
            @RequestParam String filter,
            @RequestBody ReportUpdateRequest request
    ) {
        return reportService.updateMany(filter, request);
    }

    @DeleteMapping
    public List<Long> deleteMany(@RequestParam String filter) {
        return reportService.deleteMany(filter);
    }

}