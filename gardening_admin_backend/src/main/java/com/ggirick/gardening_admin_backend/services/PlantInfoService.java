package com.ggirick.gardening_admin_backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.plant.PlantInfoDTO;
import com.ggirick.gardening_admin_backend.dto.user.UsersDomainDTO;
import com.ggirick.gardening_admin_backend.mappers.plant.PlantInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlantInfoService {
    private final ObjectMapper mapper;
    private final PlantInfoMapper plantInfoMapper;


    public record PlantQueryResult(
            List<PlantInfoDTO> list,
            int offset,
            int limit,
            int total
    ) {}

    //목록 조회
    public PlantQueryResult getPlants(String sort, String range, String filter)
            throws JsonProcessingException {
        int offset = 0;
        int limit = 10;

        if (range != null) {
            int[] r = mapper.readValue(range, int[].class);
            offset = r[0];
            limit = r[1] - r[0] + 1;
        }

        // sort 파싱
        String sortField = "createdAt";
        String sortOrder = "ASC";

        if (sort != null) {
            try {
                Map<String, String> s = mapper.readValue(
                        sort,
                        new TypeReference<Map<String, String>>() {}
                );
                sortField = s.getOrDefault("field", sortField);
                sortOrder = s.getOrDefault("order", sortOrder);
            } catch (Exception e) {
                String[] s = mapper.readValue(sort, String[].class);
                sortField = s[0];
                sortOrder = s[1];
            }
        }

        // filter 파싱
        Map<String, Object> filters = new HashMap<>();
        if (filter != null) {
            filters = mapper.readValue(filter, new TypeReference<>() {});
        }

        List<PlantInfoDTO> list =
                plantInfoMapper.findPlants(offset, limit, sortField, sortOrder, filters);
        int total = plantInfoMapper.countPlants(filters);

        return new PlantQueryResult(list, offset, limit, total);
    }

    public PlantInfoDTO createPlant(PlantInfoDTO dto) {
        plantInfoMapper.insertPlant(dto);
        return plantInfoMapper.selectOne(dto.getScientificName());
    }

    public List<PlantInfoDTO> updateMany(String filter, PlantInfoDTO dto)
            throws JsonProcessingException {

        Map<String, Object> filterMap =
                (filter != null && !filter.isEmpty())
                        ? mapper.readValue(filter, Map.class)
                        : Collections.emptyMap();

        List<String> ids = (List<String>) filterMap.get("id");
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        plantInfoMapper.updateMany(ids, dto);

        return plantInfoMapper.findMany(ids);
    }

    public List<PlantInfoDTO> deleteMany(String filter)
            throws JsonProcessingException {

        Map<String, Object> filterMap =
                (filter != null && !filter.isEmpty())
                        ? mapper.readValue(filter, Map.class)
                        : Collections.emptyMap();

        List<String> ids = (List<String>) filterMap.get("id");
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        List<PlantInfoDTO> before = plantInfoMapper.findMany(ids);
        plantInfoMapper.deleteMany(ids);

        return before;
    }
    public List<PlantInfoDTO> getMany(String filter) throws JsonProcessingException {

        Map<String, Object> filterMap =
                (filter != null && !filter.isEmpty())
                        ? mapper.readValue(filter, Map.class)
                        : Collections.emptyMap();

        List<String> ids = (List<String>) filterMap.get("id");
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        return plantInfoMapper.findMany(ids);
    }

    public PlantInfoDTO deletePlant(String scientificName) {
        PlantInfoDTO before = plantInfoMapper.selectOne(scientificName);
        if (before == null) return null;

        plantInfoMapper.deletePlant(scientificName);
        return before; // 삭제 전 데이터를 반환해야 정상 동작
    }

    public PlantInfoDTO updatePlant(PlantInfoDTO dto) {
        plantInfoMapper.updatePlant(dto);
        return plantInfoMapper.selectOne(dto.getScientificName());
    }

    public PlantInfoDTO getPlant(String scientificName) {
        return plantInfoMapper.selectOne(scientificName);
    }
}
