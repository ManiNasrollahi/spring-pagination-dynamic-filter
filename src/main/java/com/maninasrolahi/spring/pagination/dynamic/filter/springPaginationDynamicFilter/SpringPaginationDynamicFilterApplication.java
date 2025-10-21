package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository.support.CustomSimpleJpaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass =  CustomSimpleJpaRepository.class)
public class SpringPaginationDynamicFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPaginationDynamicFilterApplication.class, args);
    }

}
