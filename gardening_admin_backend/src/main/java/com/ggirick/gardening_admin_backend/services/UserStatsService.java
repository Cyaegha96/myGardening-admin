package com.ggirick.gardening_admin_backend.services;

import com.ggirick.gardening_admin_backend.dto.stats.UserStatsDTO;
import com.ggirick.gardening_admin_backend.mappers.stats.StatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatsService {

    @Autowired
    private StatsMapper statsMapper;


    public List<UserStatsDTO> getUserStats(String periodType, LocalDate startDate, LocalDate endDate) {
        switch (periodType) {
            case "DAILY":
                return statsMapper.getDailyUserStats(startDate,endDate);

            case "WEEKLY":
                return statsMapper.getWeeklyUserStats(startDate, endDate);
            case "YEARLY":
                return statsMapper.getYearlyUserStats(startDate, endDate);
            case "MONTHLY":
            default:
                return statsMapper.getMonthlyUserStats(startDate, endDate);
        }
    }

}
