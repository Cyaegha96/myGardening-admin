package com.ggirick.gardening_admin_backend.repository.custom;

import com.ggirick.gardening_admin_backend.entity.report.PlantInfoRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlantInfoRepositoryCustomImpl implements PlantInfoRepositoryCustom {
    private final EntityManager em;

    @Override
    public List<PlantInfoRequest> findAllWithPagingAndFilter(
            String status,
            String sortField,
            String sortOrder,
            int start,
            int end
    ) {
        String sortColumn = mapSortField(sortField);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT inner_table.*, ROWNUM rn FROM ( ");
        sql.append("    SELECT * FROM PLANT_INFO_REQUEST WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND STATUS = :status ");
        }

        sql.append("    ORDER BY ").append(sortColumn).append(" ").append(sortOrder);
        sql.append("  ) inner_table WHERE ROWNUM < :rowEnd ");
        sql.append(") WHERE rn >= :rowStart");

        Query query = em.createNativeQuery(sql.toString(), PlantInfoRequest.class);

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }


        int rowStart = start + 1;
        int rowEnd = end + 2;

        query.setParameter("rowStart", rowStart);
        query.setParameter("rowEnd", rowEnd);
        log.debug("🔍 CustomRepo called: status={}, sort={}, start={}, end={}",
                status, sortField, start, end);
        return query.getResultList();
    }


    @Override
    public long countFiltered(String status) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM PLANT_INFO_REQUEST WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND STATUS = :status ");
        }

        Query query = em.createNativeQuery(sql.toString());

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }

        return ((Number) query.getSingleResult()).longValue();
    }

    /**
     * React-admin 정렬 필드 → DB 컬럼명 매핑
     */
    private String mapSortField(String field) {
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
            default -> "ID"; // 기본 정렬
        };
    }
}
