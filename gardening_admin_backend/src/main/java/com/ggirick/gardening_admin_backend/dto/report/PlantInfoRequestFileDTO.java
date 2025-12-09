package com.ggirick.gardening_admin_backend.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantInfoRequestFileDTO {

    private Long id;
    private String url;
    private Long requestId;

}
