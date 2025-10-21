package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.service;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.FilterablePage;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.entity.Users;
import org.springframework.data.domain.Page;

public interface UsersService {

    Page<Users> findAllPagedWithFilters(FilterablePage request);
}
