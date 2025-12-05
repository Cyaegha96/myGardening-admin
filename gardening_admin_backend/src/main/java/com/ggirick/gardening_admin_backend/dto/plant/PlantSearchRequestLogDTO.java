package com.ggirick.gardening_admin_backend.dto.plant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantSearchRequestLogDTO {
    private int id;
    private String userUid;
    private String nickname;
    private String apiResponse;
    private String matchedScientificName;
    private Timestamp createdAt;
    private String url;
    private String commonName;
}
