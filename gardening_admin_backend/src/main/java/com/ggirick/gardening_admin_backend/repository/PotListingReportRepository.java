package com.ggirick.gardening_admin_backend.repository;


import com.ggirick.gardening_admin_backend.entity.report.PotListingReport;
import com.ggirick.gardening_admin_backend.repository.custom.PotListReportRepositoryCustom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PotListingReportRepository extends JpaRepository<PotListingReport, Long>, PotListReportRepositoryCustom {
}
