package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository.support;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.FilterablePage;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.SortOrders;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.repository.FilterablePageRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;


public class CustomSimpleJpaRepository<T> extends SimpleJpaRepository<T,Long> implements FilterablePageRepository<T> {

    public CustomSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Page<T> findAllPageWithFilters(FilterablePage filterablePage) {
        Assert.notNull(filterablePage, "filterablePage cannot be null");

        // default Specification in case that there were no filters
        Specification<T> mainSpec = Specification.unrestricted();

        if (filterablePage.hasFilters()) {
            MatchModeSpecificationResolver<T> builder = new MatchModeSpecificationResolver<>();

            //assign filter to mainSpec
            for (FilterablePage.FilterModel filter : filterablePage.getFilters()) {
                Specification<T> spec = builder.getSpecification(filter.getMatchMode(), filter.getFiledValue(), filter.getFiledName());
                if (spec != null) {
                    mainSpec = mainSpec.and(spec);
                }
            }
        }

        // default Sort in case that there were no Sort
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        if (filterablePage.hasSortField()) {
            //assign sortField to sort Object
            sort = Sort.by(filterablePage.getSortField());
        }

        if (filterablePage.hasSortOrder()) {
            //assign sortOrder to sort Object
            Sort.Direction direction = filterablePage.getSortOrder() == SortOrders.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, filterablePage.getSortField());
        }

        PageRequest pageRequest = PageRequest.of(
                filterablePage.getPage(),
                filterablePage.getRows(),
                sort
        );

        return findAll(mainSpec, pageRequest);
    }
}
