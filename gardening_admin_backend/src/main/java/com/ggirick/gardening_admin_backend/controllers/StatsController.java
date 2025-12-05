package com.ggirick.gardening_admin_backend.controllers;

import com.ggirick.gardening_admin_backend.dto.plant.PlantSearchRequestLogDTO;
import com.ggirick.gardening_admin_backend.dto.session.ActiveSessionDTO;
import com.ggirick.gardening_admin_backend.dto.stats.DailyQuotaResponse;
import com.ggirick.gardening_admin_backend.dto.stats.UserStatsDTO;
import com.ggirick.gardening_admin_backend.services.PlantnetStatsService;
import com.ggirick.gardening_admin_backend.services.RedisService;
import com.ggirick.gardening_admin_backend.services.UserStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {


    private final UserStatsService userStatsService;


    private final PlantnetStatsService plantnetStatsService;

    private final RedisService redisService;

    @GetMapping("/users")
    public List<UserStatsDTO> getUserStats(
            @RequestParam(defaultValue = "MONTH") String periodType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {

        return userStatsService.getUserStats(periodType.toUpperCase(), startDate, endDate);
    }
    @GetMapping("/plantnet")
    public ResponseEntity<DailyQuotaResponse> getDailyQuota(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day
    ) throws IOException {
        if (day == null) {
            day = LocalDate.now();
        }
        System.out.println("검색 날짜:"+ day.toString());
        return plantnetStatsService.getDailyQuota(day.toString())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @GetMapping("/searchPlantLog")
    public List<PlantSearchRequestLogDTO> getSearchPlantLogByDate(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day
    ) throws IOException {
        String dayStr = (day != null) ? day.toString() : LocalDate.now().toString();
        return plantnetStatsService.getPlantSearchRequestLog(dayStr);
    }



}
