package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.filterablePageTest;

import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.FilterablePage;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.MatchModes;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.SortOrders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilterablePageTest {


    private FilterablePage page;

    @BeforeEach
    void setUp() {
        page = FilterablePage.pageOf(1, 10);
    }

    @Test
    void testPageOf_CreatesWithCorrectValues() {
        assertEquals(1, page.getPage());
        assertEquals(10, page.getRows());
        assertNotNull(page.getFilters());
        assertTrue(page.getFilters().isEmpty());
    }

    @Test
    void testWithSort_SetsSortFieldsCorrectly() {
        page.witSort("name", SortOrders.ASC);
        assertEquals("name", page.getSortField());
        assertEquals(SortOrders.ASC, page.getSortOrder());
    }

    @Test
    void testWithSort_NullSortField_ThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> page.witSort(null, SortOrders.ASC));
        assertTrue(ex.getMessage().contains("sortField cannot be null"));
    }

    @Test
    void testWithSort_NullSortOrder_ThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> page.witSort("name", null));
        assertTrue(ex.getMessage().contains("sortOrder cannot be null"));
    }

    @Test
    void testAddFilter_WithDefaultMatchMode() {
        page.addFilter("status", "ACTIVE");

        assertTrue(page.hasFilters());
        assertEquals(1, page.getFilters().size());

        FilterablePage.FilterModel filter = page.getFilters().getFirst();
        assertEquals("status", filter.getFiledName());
        assertEquals("ACTIVE", filter.getFiledValue());
        assertEquals(MatchModes.EQUALS, filter.getMatchMode());
    }

    @Test
    void testAddFilter_WithSpecificMatchMode() {
        page.addFilter("age", 25, MatchModes.GREATER_THAN);
        assertEquals(1, page.getFilters().size());
        FilterablePage.FilterModel filter = page.getFilters().getFirst();
        assertEquals("age", filter.getFiledName());
        assertEquals(25, filter.getFiledValue());
        assertEquals(MatchModes.GREATER_THAN, filter.getMatchMode());
    }

    @Test
    void testAddFilter_NullField_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> page.addFilter(null, "value"));
    }

    @Test
    void testAddFilter_NullValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> page.addFilter("field", null));
    }

    @Test
    void testAddFilter_NullMatchMode_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> page.addFilter("field", "value", null));
    }

    @Test
    void testAddFilters_AddsAllFilters() {
        FilterablePage.FilterModel f1 = new FilterablePage.FilterModel("name", "John", MatchModes.EQUALS);
        FilterablePage.FilterModel f2 = new FilterablePage.FilterModel("age", 30, MatchModes.GREATER_THAN);

        page.addFilters(List.of(f1, f2));

        assertEquals(2, page.getFilters().size());
        assertTrue(page.hasFilters());
    }

    @Test
    void testAddFilters_NullList_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> page.addFilters(null));
    }

    @Test
    void testRemoveFilters_ClearsFilters() {
        page.addFilter("a", 1);
        page.addFilter("b", 2);

        assertTrue(page.hasFilters());

        page.removeFilters();
        assertFalse(page.hasFilters());
        assertEquals(0, page.getFilters().size());
    }

    @Test
    void testHasSortOrder_ReturnsTrueIfSet() {
        page.witSort("id", SortOrders.ASC);
        assertTrue(page.hasSortOrder());
    }

    @Test
    void testHasSortOrder_ReturnsFalseIfNotSet() {
        assertFalse(page.hasSortOrder());
    }

    @Test
    void testHasSortField_ReturnsTrueIfSet() {
        page.witSort("id", SortOrders.ASC);
        assertTrue(page.hasSortField());
    }

    @Test
    void testHasSortField_ReturnsFalseIfEmpty() {
        assertFalse(page.hasSortField());
        page.setSortField("");
        assertFalse(page.hasSortField());
    }

    @Test
    void testFluentApi_ChainingWorks() {
        page = FilterablePage.pageOf(1, 5)
                .witSort("name", SortOrders.DESC)
                .addFilter("active", true)
                .addFilter("age", 20, MatchModes.GREATER_THAN);

        assertTrue(page.hasSortField());
        assertTrue(page.hasSortOrder());
        assertEquals(2, page.getFilters().size());
    }
}
