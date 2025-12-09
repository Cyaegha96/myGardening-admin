package com.ggirick.gardening_admin_backend.dto.report;

import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequestFile;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantInfoRequestResponse {
    private Long id;
    private String scientificName;
    private String changes;
    private String reviewerUid;
    private String reviewNote;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userUid;
    private List<PlantInfoRequestFile> files;

}
