package com.ggirick.gardening_admin_backend.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotListingReportCreateRequest {
    @NotNull(message = "요청 대상 id는 필수입니다.")
    Long potListingId;
    @NotBlank(message = "이유는 필수입니다.")
    String reason;
}
