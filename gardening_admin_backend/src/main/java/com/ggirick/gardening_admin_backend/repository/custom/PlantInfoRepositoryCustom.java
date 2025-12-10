package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;

import java.util.List;

public interface PlantInfoRepositoryCustom {
    List<PlantInfoRequest> findAllWithPagingAndFilter(String status, String sortField, String sortOrder, int start, int end);
    long countFiltered(String status);
}