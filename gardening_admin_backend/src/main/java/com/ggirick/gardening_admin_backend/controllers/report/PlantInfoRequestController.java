package com.ggirick.gardening_admin_backend.controllers.report;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.auth.UserTokenDTO;
import com.ggirick.gardening_admin_backend.dto.report.PlantInfoRequestCreateRequest;
import com.ggirick.gardening_admin_backend.dto.report.PlantInfoRequestResponse;
import com.ggirick.gardening_admin_backend.dto.report.PlantInfoRequestUpdateRequest;
import com.ggirick.gardening_admin_backend.dto.report.ReportResponse;
import com.ggirick.gardening_admin_backend.services.report.PlantInfoRequestService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("/plantinforequest")
@RequiredArgsConstructor
public class PlantInfoRequestController {

    private final PlantInfoRequestService service;
    private final ObjectMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PlantInfoRequestResponse createRequest(
            @RequestPart("request") PlantInfoRequestCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal UserTokenDTO userInfo
    ) throws Exception {
        return service.createRequest(request, files, userInfo.getUid());
    }

    @GetMapping("/{id}")
    public PlantInfoRequestResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public ResponseEntity<List<PlantInfoRequestResponse>> getAll(
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


        Page<PlantInfoRequestResponse> page =
                service.getAllWithFilter(status, start, end, sortField, sortOrder);

        long total = page.getTotalElements();
        List<PlantInfoRequestResponse> content = page.getContent();

        response.setHeader(
                "Content-Range",
                String.format("plantinforequest %d-%d/%d",
                        start,
                        start + content.size() - 1,
                        total
                )
        );
        response.setHeader("Access-Control-Expose-Headers", "Content-Range");

        return ResponseEntity.ok().body(content);
    }


    @PutMapping("/{id}")
    public PlantInfoRequestResponse update(
            @PathVariable Long id,
            @RequestBody PlantInfoRequestUpdateRequest request,
            @AuthenticationPrincipal UserTokenDTO userInfo
    ) {
        System.out.println("업데이트");
        return service.update(id, request, userInfo.getUid());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}