package com.ggirick.gardening_admin_backend.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportUpdateRequest {

    private Long id;

    @NotBlank(message = "상태 값은 필수입니다.")
    private String status;

    private String reason; // optional
}