package com.ggirick.gardening_admin_backend.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseRepositoryCustomImpl<T> {

    protected final EntityManager em;

    // 각 엔티티에서 구현
    protected abstract String getTableName();
    protected abstract Class<T> getEntityClass();
    protected abstract String mapSortField(String field);

    public List<T> findAllWithPagingAndFilter(String status, String sortField, String sortOrder, int start, int end) {
        String sortColumn = mapSortField(sortField);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT inner_table.*, ROWNUM rn FROM ( ");
        sql.append("    SELECT * FROM ").append(getTableName()).append(" WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND STATUS = :status ");
        }

        sql.append("    ORDER BY ").append(sortColumn).append(" ").append(sortOrder);
        sql.append("  ) inner_table WHERE ROWNUM <= :end ");
        sql.append(") WHERE rn > :start");

        Query query = em.createNativeQuery(sql.toString(), getEntityClass());

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }

        query.setParameter("start", start);
        query.setParameter("end", end);

        return query.getResultList();
    }

    public long countFiltered(String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(getTableName()).append(" WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND STATUS = :status ");
        }

        Query query = em.createNativeQuery(sql.toString());

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }

        return ((Number) query.getSingleResult()).longValue();
    }
}