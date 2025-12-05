package com.ggirick.gardening_admin_backend.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsDTO {
    private String period;   // "2025-01", "2025-12-03", "2025" 등 다양한 단위 표현
    private int count;       // 해당 기간의 사용자 수

    // 확장성을 위해 periodType을 추가
    private String periodType; // "MONTH", "DAY", "YEAR"

}
