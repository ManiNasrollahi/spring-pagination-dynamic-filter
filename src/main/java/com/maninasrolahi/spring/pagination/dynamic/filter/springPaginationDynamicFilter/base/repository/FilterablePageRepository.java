package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.FilterablePage;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface FilterablePageRepository<T> {

    Page<T> findAllPageWithFilters(FilterablePage filterablePage);
}
