package com.ggirick.gardening_admin_backend.repository;


import com.ggirick.gardening_admin_backend.entity.report.Report;
import com.ggirick.gardening_admin_backend.repository.custom.ReportRepositoryCustom;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {}