package com.ggirick.gardening_admin_backend.mappers.plant;

import com.ggirick.gardening_admin_backend.dto.plant.PlantInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlantInfoMapper {
    List<PlantInfoDTO> findPlants(int offset, int limit, String sortField, String sortOrder, Map<String, Object> filters);

    int countPlants(Map<String, Object> filters);

    PlantInfoDTO selectOne(String scientificName);

    void insertPlant(PlantInfoDTO dto);

    void updateMany(List<String> ids, PlantInfoDTO dto);

    List<PlantInfoDTO> findMany(List<String> ids);

    void deleteMany(List<String> ids);

    void updatePlant(PlantInfoDTO dto);

    void deletePlant(String scientificName);
}
