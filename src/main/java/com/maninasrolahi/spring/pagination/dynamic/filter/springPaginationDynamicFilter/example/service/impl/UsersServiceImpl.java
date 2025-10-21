package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.service.impl;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.FilterablePage;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.entity.Users;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.repository.UsersRepository;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;

    @Override
    public Page<Users> findAllPagedWithFilters(FilterablePage request) {
        return repository.findAllPageWithFilters(request);
    }
}
