package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SortOrders {

    DESC("desc"),
    ASC("asc");

    private final String value;

    SortOrders(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static SortOrders fromValue(String value) {
        if (value == null) return null;
        for (SortOrders mode : values()) {
            if (mode.value.equalsIgnoreCase(value.trim())) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown MatchMode: " + value);
    }
}
