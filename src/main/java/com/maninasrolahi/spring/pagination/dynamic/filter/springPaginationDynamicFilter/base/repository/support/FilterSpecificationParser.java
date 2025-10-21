package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository.support;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FilterSpecificationParser<T> {

    public Specification<T> getListInSpecification(Object value, String fieldName) {
        return (root, _, _) -> {
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                return (root.get(fieldName).in(list));
            }else
                return null;
        };
    }

    
    public Specification<T> getListNotInSpecification(Object value, String fieldName) {
        return (root, _, _) -> {
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                return root.get(fieldName).in(list).not();
            }else
                return null;
        };
    }

    
    public Specification<T> getStringContainSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.like(root.get(fieldName), "%" + value + "%");
    }

    
    public Specification<T> getStringNotContainSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.notLike(root.get(fieldName), "%" + value + "%");
    }

    
    public Specification<T> getStringEndWithSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.like(root.get(fieldName), "%" + value);
    }

    
    public Specification<T> getStringStartWithSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.like(root.get(fieldName), value + "%");
    }

    
    public Specification<T> getEqualsSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldName), value);
    }

    
    public Specification<T> getNotEqualsSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(fieldName), value);
    }

    
    public Specification<T> getNumericLessThanSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) ->{
            Number number = (Number) value;
            return switch (number) {
                case BigDecimal bigDecimal ->
                        criteriaBuilder.lessThan(root.get(fieldName), bigDecimal);
                case Long l ->
                        criteriaBuilder.lessThan(root.get(fieldName), l);
                case Integer i ->
                        criteriaBuilder.lessThan(root.get(fieldName), i);
                case Double d ->
                        criteriaBuilder.lessThan(root.get(fieldName), d);
                case null, default ->
                        null;
            };
        };
    }

    
    public Specification<T> getNumericLessThanOrEqualToSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            Number number = (Number) value;
            return switch (number) {
                case BigDecimal bigDecimal ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), bigDecimal);
                case Long l ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), l);
                case Integer i ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), i);
                case Double d ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), d);
                case null, default ->
                        null;
            };
        };
    }

    
    public Specification<T> getNumericGreaterThanSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            Number number = (Number) value;
            return switch (number) {
                case BigDecimal bigDecimal ->
                        criteriaBuilder.greaterThan(root.get(fieldName), bigDecimal);
                case Long l ->
                        criteriaBuilder.greaterThan(root.get(fieldName), l);
                case Integer i ->
                        criteriaBuilder.greaterThan(root.get(fieldName), i);
                case Double d ->
                        criteriaBuilder.greaterThan(root.get(fieldName), d);
                case null, default ->
                        null;
            };
        };
    }

    
    public Specification<T> getNumericGreaterThanOrEqualToSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            Number number = (Number) value;
            return switch (number) {
                case BigDecimal bigDecimal ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), bigDecimal);
                case Long l ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), l);
                case Integer i ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), i);
                case Double d ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), d);
                case null, default -> null;
            };
        };
    }

    
    public Specification<T> getDateIsSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return criteriaBuilder.equal(root.get(fieldName), dateFormat.parse(value.toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
    }

    
    public Specification<T> getDateIsNotSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return criteriaBuilder.notEqual(root.get(fieldName), dateFormat.parse(value.toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
    }

    
    public Specification<T> getDateBeforeSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName),dateFormat.parse(value.toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
    }

    
    public Specification<T> getDateAfterSpecification(Object value, String fieldName) {
        return (root, _, criteriaBuilder) -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), dateFormat.parse(value.toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
