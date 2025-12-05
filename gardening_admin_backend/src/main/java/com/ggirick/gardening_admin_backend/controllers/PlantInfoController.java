package com.ggirick.gardening_admin_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ggirick.gardening_admin_backend.dto.plant.PlantInfoDTO;
import com.ggirick.gardening_admin_backend.services.PlantInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/plants")
@RequiredArgsConstructor
public class PlantInfoController {

    private final PlantInfoService plantInfoService;

    /**
     * --------------------------
     * LIST: GET /plants
     * --------------------------
     */
    @GetMapping
    public List<PlantInfoDTO> getPlants(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String filter,
            HttpServletResponse response
    ) throws JsonProcessingException {

        PlantInfoService.PlantQueryResult result =
                plantInfoService.getPlants(sort, range, filter);

        response.setHeader(
                "Content-Range",
                String.format("plants %d-%d/%d",
                        result.offset(),
                        result.offset() + result.list().size() - 1,
                        result.total())
        );

        response.setHeader("Access-Control-Expose-Headers", "Content-Range");
        return result.list();
    }

    /**
     * --------------------------
     * GET ONE: GET /plants/{id}
     * id = scientificName
     * --------------------------
     */
    @GetMapping("/{id}")
    public PlantInfoDTO getPlant(@PathVariable("id") String scientificName) {
        return plantInfoService.getPlant(scientificName);
    }

    /**
     * --------------------------
     * UPDATE ONE: PUT /plants/{id}
     * --------------------------
     */
    @PutMapping("/{id}")
    public PlantInfoDTO updatePlant(
            @PathVariable("id") String scientificName,
            @RequestBody PlantInfoDTO dto
    ) {
        dto.setScientificName(scientificName);
        return plantInfoService.updatePlant(dto);
    }

    /**
     * --------------------------
     * DELETE ONE: DELETE /plants/{id}
     * --------------------------
     */
    @DeleteMapping("/{id}")
    public PlantInfoDTO deletePlant(@PathVariable("id") String scientificName) {
        log.info("식물 삭제 호출: {}", scientificName);
        return plantInfoService.deletePlant(scientificName);
    }

    /**
     * --------------------------
     * GET MANY: GET /plants/batch
     * --------------------------
     */
    @GetMapping("/batch")
    public List<PlantInfoDTO> getMany(@RequestParam(required = false) String filter)
            throws JsonProcessingException {
        return plantInfoService.getMany(filter);
    }

    /**
     * --------------------------
     * UPDATE MANY: PUT /plants
     * --------------------------
     */
    @PutMapping
    public List<PlantInfoDTO> updateMany(
            @RequestParam(required = false) String filter,
            @RequestBody PlantInfoDTO dto
    ) throws JsonProcessingException {
        return plantInfoService.updateMany(filter, dto);
    }

    /**
     * --------------------------
     * DELETE MANY: DELETE /plants
     * --------------------------
     */
    @DeleteMapping
    public List<PlantInfoDTO> deleteMany(
            @RequestParam(required = false) String filter
    ) throws JsonProcessingException {
        return plantInfoService.deleteMany(filter);
    }

    /**
     * --------------------------
     * CREATE: POST /plants
     * --------------------------
     */
    @PostMapping
    public PlantInfoDTO createPlant(@RequestBody PlantInfoDTO dto) {
        return plantInfoService.createPlant(dto);
    }
}