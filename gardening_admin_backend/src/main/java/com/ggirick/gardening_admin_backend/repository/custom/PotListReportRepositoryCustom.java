package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;
import com.ggirick.gardening_admin_backend.entity.report.PotListingReport;

import java.util.List;

public interface PotListReportRepositoryCustom {
    List<PotListingReport> findAllWithPagingAndFilter(String status, String sortField, String sortOrder, int start, int end);
    long countFiltered(String status);
}
