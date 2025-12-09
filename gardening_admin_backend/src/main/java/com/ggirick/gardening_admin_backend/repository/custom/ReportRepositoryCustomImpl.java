package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.Report;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Report> findAllWithPagingAndFilter(
            String status,
            String sortField,
            String sortOrder,
            int start,
            int end
    ) {
        // 🔥 React-admin 필드명 → DB 컬럼명 매핑
        String sortColumn = mapSortField(sortField);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT inner_table.*, ROWNUM rn FROM ( ");
        sql.append("    SELECT * FROM report WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = :status ");
        }

        sql.append("    ORDER BY ").append(sortColumn).append(" ").append(sortOrder);
        sql.append("  ) inner_table WHERE ROWNUM <= :end ");
        sql.append(") WHERE rn > :start");

        Query query = em.createNativeQuery(sql.toString(), Report.class);

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }

        query.setParameter("start", start);
        query.setParameter("end", end);

        return query.getResultList();
    }

    @Override
    public long countFiltered(String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM report WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = :status ");
        }

        Query query = em.createNativeQuery(sql.toString());

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }

        return ((Number) query.getSingleResult()).longValue();
    }

    //  필드명 매핑
    private String mapSortField(String field) {
        return switch (field) {
            case "id" -> "ID";
            case "reason" -> "REASON";
            case "status" -> "STATUS";
            case "targetId" -> "TARGET_ID";
            case "targetType" -> "TARGET_TYPE";
            case "reporterUid" -> "REPORTER_UID";
            case "createdAt" -> "CREATED_AT";
            default -> "ID"; // 기본값
        };
    }
}
