package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.Report;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepositoryCustomImpl
        extends BaseRepositoryCustomImpl<Report>
        implements ReportRepositoryCustom {

    public ReportRepositoryCustomImpl(EntityManager em) { super(em); }

    @Override
    protected String getTableName() { return "REPORT"; }

    @Override
    protected Class<Report> getEntityClass() { return Report.class; }

    @Override
    protected String mapSortField(String field) {
        return switch (field) {
            case "id" -> "ID";
            case "reason" -> "REASON";
            case "status" -> "STATUS";
            case "targetId" -> "TARGET_ID";
            case "targetType" -> "TARGET_TYPE";
            case "reporterUid" -> "REPORTER_UID";
            case "createdAt" -> "CREATED_AT";
            default -> "ID";
        };
    }
}
