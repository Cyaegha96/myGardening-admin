package com.ggirick.gardening_admin_backend.mappers.stats;

import com.ggirick.gardening_admin_backend.dto.plant.PlantSearchRequestLogDTO;
import com.ggirick.gardening_admin_backend.dto.stats.UserStatsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatsMapper {

    
    //사용자 가입 정보 통계
    List<UserStatsDTO> getWeeklyUserStats(LocalDate startDate, LocalDate endDate);

    List<UserStatsDTO> getYearlyUserStats(LocalDate startDate, LocalDate endDate);

    List<UserStatsDTO> getMonthlyUserStats(LocalDate startDate, LocalDate endDate);

    List<UserStatsDTO> getDailyUserStats(LocalDate startDate, LocalDate endDate);


    List<PlantSearchRequestLogDTO> getSearchPlantLogByDate(String date);
}
