package com.ggirick.gardening_admin_backend.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantInfoRequestUpdateRequest {

    private Long id;


    @NotBlank(message = "변경사항은 필수입니다.")
    private String status;

    private String reviewerUid;
    @NotBlank(message = "변경사항은 필수입니다.")
    private String reviewNote;

}