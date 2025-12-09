package com.ggirick.gardening_admin_backend.repository;


import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequestFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantInfoRequestFileRepository extends JpaRepository<PlantInfoRequestFile, Long> {
    List<PlantInfoRequestFile> findByRequestId(Long requestId);
}