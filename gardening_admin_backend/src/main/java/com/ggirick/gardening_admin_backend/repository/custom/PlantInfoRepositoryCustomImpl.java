package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PlantInfoRepositoryCustomImpl
        extends BaseRepositoryCustomImpl<PlantInfoRequest>
        implements  PlantInfoRepositoryCustom {

    public PlantInfoRepositoryCustomImpl(EntityManager em) {
        super(em);
    }

    @Override
    protected String getTableName() {
        return "PLANT_INFO_REQUEST";
    }

    @Override
    protected Class<PlantInfoRequest> getEntityClass() {
        return PlantInfoRequest.class;
    }

    @Override
    protected String mapSortField(String field) {
        return switch (field) {
            case "id" -> "ID";
            case "createdAt" -> "CREATED_AT";
            case "scientificName" -> "PLANT_SCIENTIFIC_NAME";
            case "changes" -> "REQUESTED_CHANGES";
            case "reviewerUid" -> "REVIEWER_UID";
            case "reviewNote" -> "REVIEW_NOTE";
            case "status" -> "STATUS";
            case "updatedAt" -> "UPDATED_AT";
            case "userUid" -> "USER_UID";
            default -> "ID";
        };
    }
}