package com.ggirick.gardening_admin_backend.dto.plant;

import com.ggirick.gardening_admin_backend.dto.tag.PlantTagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantInfoDTO {
    private String id;
    private String scientificName;
    private String commonName;
    private String family;
    private String genus;
    private String origin;
    private String environment;
    private String light;
    private String temperatureHumidity;
    private String watering;
    private String soil;
    private String fertilizer;
    private String potRepot;
    private String propagation;
    private String pestsTips;
    private String commonUses;
    private String culturalSignificance;
    private String description;
    private String sampleImageUrl;
    private List<PlantTagDTO> tags;
}
