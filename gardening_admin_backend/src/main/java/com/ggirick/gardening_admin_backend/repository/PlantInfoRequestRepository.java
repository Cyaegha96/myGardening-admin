package com.ggirick.gardening_admin_backend.repository;


import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;
import com.ggirick.gardening_admin_backend.repository.custom.PlantInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantInfoRequestRepository extends JpaRepository<PlantInfoRequest, Long>, PlantInfoRepositoryCustom {}
