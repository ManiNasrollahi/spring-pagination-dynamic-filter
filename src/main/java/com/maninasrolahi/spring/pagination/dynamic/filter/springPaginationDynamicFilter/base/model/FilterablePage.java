package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterablePage {

    private int page;
    private int rows;

    private String sortField;

    private SortOrders sortOrder;

    private final List<FilterModel> filters = new ArrayList<>();

    private FilterablePage(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public static FilterablePage pageOf(int page, int rows) {
        return new FilterablePage(page,rows);
    }

    public FilterablePage witSort(String sortField, SortOrders sortOrders) {
        Assert.notNull(sortField, "sortField cannot be null");
        Assert.notNull(sortOrders, "sortOrder cannot be null");
        this.sortField = sortField;
        this.sortOrder = sortOrders;
        return this;
    }

    public FilterablePage addFilter(String filedName, Object filedValue) {
        Assert.notNull(filedName, "filedName cannot be null");
        Assert.notNull(filedValue, "filedValue cannot be null");
        this.filters.add(new FilterModel(filedName,filedValue,MatchModes.EQUALS));
        return this;
    }

    public FilterablePage addFilter(String filedName, Object filedValue, MatchModes matchMode) {
        Assert.notNull(filedName, "filedName cannot be null");
        Assert.notNull(filedValue, "filedValue cannot be null");
        Assert.notNull(matchMode, "matchMode cannot be null");
        this.filters.add(new FilterModel(filedName,filedValue,matchMode));
        return this;
    }

    public void addFilters(List<FilterModel> filters) {
        Assert.notNull(filters, "filters cannot be null");
        this.filters.addAll(filters);
    }

    public void removeFilters() {
        this.filters.clear();
    }

    public boolean hasSortOrder(){
        return this.sortOrder != null;
    }

    public boolean hasSortField(){
        return this.sortField != null && !this.sortField.isEmpty();
    }

    public boolean hasFilters(){
        return !this.filters.isEmpty();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Data
    public static class FilterModel {

        private String filedName;
        private Object filedValue;
        private MatchModes matchMode;

    }

}






