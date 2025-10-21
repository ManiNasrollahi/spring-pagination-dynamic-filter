package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.repository;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository.FilterablePageRepository;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>, FilterablePageRepository<Users> {
}
