package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;
import com.ggirick.gardening_admin_backend.entity.report.PotListingReport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PotListReportRepositoryCustomImpl
        extends BaseRepositoryCustomImpl<PotListingReport>
        implements  PotListReportRepositoryCustom{

    public PotListReportRepositoryCustomImpl(EntityManager em) { super(em); }

    @Override
    protected String getTableName() { return "POT_LISTING_REPORT"; }

    @Override
    protected Class<PotListingReport> getEntityClass() { return PotListingReport.class; }

    @Override
    protected String mapSortField(String field) {
        return switch (field) {
            case "id" -> "ID";
            case "potListingId" -> "POT_LISTING_ID";
            case "reason" -> "REASON";
            case "reporterUid" -> "REPORTER_UID";
            case "status" -> "STATUS";
            case "createdAt" -> "CREATED_AT";
            default -> "ID";
        };
    }
}
