package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.controller;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.FilterablePage;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.entity.Users;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.example.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/search")
    public ResponseEntity<Page<Users>> searchUsers(@RequestBody FilterablePage request) {
        Assert.notNull(request, "request cannot be null");
        Page<Users> result = usersService.findAllPagedWithFilters(request);
        return ResponseEntity.ok(result);
    }

}
