package com.ggirick.gardening_admin_backend.dto.report;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotListingReportResponse {

    private Long id;

    private LocalDateTime createdAt;

    private Long potListingId;

    private String reason;

    private String reporterUid;

    private String status;
}