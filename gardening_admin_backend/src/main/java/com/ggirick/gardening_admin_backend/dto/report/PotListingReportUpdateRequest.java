package com.ggirick.gardening_admin_backend.dto.report;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PotListingReportUpdateRequest {

    private Long id;        // 어떤 신고를 변경할지 식별
    private String status;  // 변경할 상태 (approved / rejected / pending)
}