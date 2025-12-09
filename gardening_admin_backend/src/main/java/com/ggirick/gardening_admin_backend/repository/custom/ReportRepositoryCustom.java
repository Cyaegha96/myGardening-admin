package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.Report;

import java.util.List;

public interface ReportRepositoryCustom {
    List<Report> findAllWithPagingAndFilter(String status, String sortField, String sortOrder, int start, int end);
    long countFiltered(String status);
}