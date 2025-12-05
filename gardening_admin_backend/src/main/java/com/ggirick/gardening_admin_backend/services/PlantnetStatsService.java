package com.ggirick.gardening_admin_backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.plant.PlantSearchRequestLogDTO;
import com.ggirick.gardening_admin_backend.dto.stats.DailyQuotaResponse;
import com.ggirick.gardening_admin_backend.mappers.stats.StatsMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class PlantnetStatsService {
    @Value("${plantnet.api.key}") // 예시: application.properties에 plantnet.api.key=2b10hQU1Qz8dp82c7WCUuuMu
    private String API_KEY;

    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://my-api.plantnet.org/v2/quota/daily";
    private final OkHttpClient httpClient = new OkHttpClient();

    private final StatsMapper statsMapper;

    public Optional<DailyQuotaResponse> getDailyQuota(String day) throws IOException {
        if (day == null || day.isEmpty()) {
            day = LocalDate.now().toString();
        }

        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("day", day)
                .addQueryParameter("api-key", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                System.err.println("Quota 조회 실패: " + response.code());
                return Optional.empty();
            }

            String responseBody = response.body().string();
            DailyQuotaResponse dto = objectMapper.readValue(responseBody, DailyQuotaResponse.class);
            return Optional.of(dto);
        }
    }

    public List<PlantSearchRequestLogDTO> getPlantSearchRequestLog(String day) throws IOException {
        if (day == null || day.isEmpty()) {
            day = LocalDate.now().toString();
        }

        return statsMapper.getSearchPlantLogByDate(day);
    }
}
