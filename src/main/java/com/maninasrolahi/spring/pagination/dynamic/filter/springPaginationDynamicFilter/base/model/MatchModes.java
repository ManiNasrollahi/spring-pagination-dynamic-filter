package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MatchModes {

    // Text-based
    STARTS_WITH("startsWith"),
    CONTAINS("contains"),
    NOT_CONTAINS("notContains"),
    ENDS_WITH("endsWith"),

    // Numeric
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL_TO("lte"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),

    // Numeric and String
    EQUALS("equals"),
    NOT_EQUALS("notEquals"),

    // List
    IN("in"),
    NOT_IN("notIn"),

    // Date
    DATE_IS("dateIs"),
    DATE_IS_NOT("dateIsNot"),
    DATE_BEFORE("dateBefore"),
    DATE_AFTER("dateAfter");

    private final String value;

    MatchModes(String value) {
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
    public static MatchModes fromValue(String value) {
        if (value == null) return null;
        for (MatchModes mode : values()) {
            if (mode.value.equalsIgnoreCase(value.trim())) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown MatchMode: " + value);
    }
}
