package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository.support;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.MatchModes;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

public class MatchModeSpecificationResolver<T> {

    public Specification<T> getSpecification(MatchModes matchMode, Object value, String fieldName) {
        Assert.notNull(matchMode, "matchMode cannot be null");
        Assert.notNull(value, "value cannot be null");
        Assert.notNull(fieldName,"fieldName cannot be null");

        FilterSpecificationParser<T> parser = new FilterSpecificationParser<>();

        return switch (matchMode) {
            case STARTS_WITH -> parser.getStringStartWithSpecification(value, fieldName);
            case CONTAINS -> parser.getStringContainSpecification(value, fieldName);
            case NOT_CONTAINS -> parser.getStringNotContainSpecification(value, fieldName);
            case ENDS_WITH -> parser.getStringEndWithSpecification(value, fieldName);
            case LESS_THAN -> parser.getNumericLessThanSpecification(value, fieldName);
            case LESS_THAN_OR_EQUAL_TO -> parser.getNumericLessThanOrEqualToSpecification(value, fieldName);
            case GREATER_THAN -> parser.getNumericGreaterThanSpecification(value, fieldName);
            case GREATER_THAN_OR_EQUAL_TO -> parser.getNumericGreaterThanOrEqualToSpecification(value, fieldName);
            case EQUALS -> parser.getEqualsSpecification(value, fieldName);
            case NOT_EQUALS -> parser.getNotEqualsSpecification(value, fieldName);
            case IN -> parser.getListInSpecification(value, fieldName);
            case NOT_IN -> parser.getListNotInSpecification(value, fieldName);
            case DATE_IS -> parser.getDateIsSpecification(value, fieldName);
            case DATE_IS_NOT -> parser.getDateIsNotSpecification(value, fieldName);
            case DATE_BEFORE -> parser.getDateBeforeSpecification(value, fieldName);
            case DATE_AFTER -> parser.getDateAfterSpecification(value, fieldName);
        };
    }

}
