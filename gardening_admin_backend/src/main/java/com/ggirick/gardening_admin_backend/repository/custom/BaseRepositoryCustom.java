package com.ggirick.gardening_admin_backend.repository.custom;

import java.util.List;
//페이징 처리를 위한 공통 메서드를 만드는데 결국에 다 똑같은 메서드를 만들길래
//공통 인터페이스를 따로 정의함
public interface BaseRepositoryCustom<T> {
    List<T> findAllWithPagingAndFilter(String status, String sortField, String sortOrder, int start, int end);
    long countFiltered(String status);
}