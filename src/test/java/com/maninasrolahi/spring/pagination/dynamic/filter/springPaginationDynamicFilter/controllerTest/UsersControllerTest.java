package com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maninasrolahi.spring.pagination.dynamic.filter.springPaginationDynamicFilter.base.model.MatchModes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void performAndAssertWithoutFilters(String jsonBody) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(response);

        assertTrue(root.has("content"), "Response must contain 'content' field");

        JsonNode dataArray = root.get("content");
        assertTrue(dataArray.isArray(), "'data' field must be an array");

        // Fail if empty
        if (dataArray.isEmpty()) {
            fail("Response data is empty — expected matching results");
        }
    }
    
    private void performAndAssertWithFilters(String jsonBody) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(response);

        assertTrue(root.has("content"), "Response must contain 'content' field");

        JsonNode dataArray = root.get("content");
        assertTrue(dataArray.isArray(), "'data' field must be an array");

        // Fail if empty
        if (dataArray.isEmpty()) {
            fail("Response data is empty — expected matching results");
        }

        // Extract filters from request to validate results
        JsonNode requestRoot = objectMapper.readTree(jsonBody);
        JsonNode filters = requestRoot.path("filters");
        assertTrue(filters.isArray(), "Filters must be array in request");

        for (JsonNode filter : filters) {
            String fieldName = filter.get("filedName").asText();
            JsonNode fieldValueNode = filter.get("filedValue");
            String matchMode = filter.get("matchMode").asText();

            for (JsonNode record : dataArray) {
                if (!record.has(fieldName)) continue;

                String recordValue = record.get(fieldName).asText();

                boolean match = switch (matchMode.toUpperCase()) {
                    case "STARTSWITH" -> recordValue.startsWith(fieldValueNode.asText());
                    case "CONTAINS" -> recordValue.contains(fieldValueNode.asText());
                    case "NOT_CONTAINS" -> !recordValue.contains(fieldValueNode.asText());
                    case "ENDS_WITH" -> recordValue.endsWith(fieldValueNode.asText());
                    case "EQUALS" -> recordValue.equals(fieldValueNode.asText());
                    case "NOT_EQUALS" -> !recordValue.equals(fieldValueNode.asText());
                    case "IN" -> fieldValueNode.isArray() && containsAny(fieldValueNode, recordValue);
                    case "NOT_IN" -> fieldValueNode.isArray() && !containsAny(fieldValueNode, recordValue);
                    case "LESS_THAN" -> toDouble(recordValue) < fieldValueNode.asDouble();
                    case "LESS_THAN_OR_EQUAL_TO" -> toDouble(recordValue) <= fieldValueNode.asDouble();
                    case "GREATER_THAN" -> toDouble(recordValue) > fieldValueNode.asDouble();
                    case "GREATER_THAN_OR_EQUAL_TO" -> toDouble(recordValue) >= fieldValueNode.asDouble();
                    case "DATE_IS" -> LocalDate.parse(recordValue).equals(LocalDate.parse(fieldValueNode.asText()));
                    case "DATE_IS_NOT" -> !LocalDate.parse(recordValue).equals(LocalDate.parse(fieldValueNode.asText()));
                    case "DATE_BEFORE" -> LocalDate.parse(recordValue).isBefore(LocalDate.parse(fieldValueNode.asText()));
                    case "DATE_AFTER" -> LocalDate.parse(recordValue).isAfter(LocalDate.parse(fieldValueNode.asText()));
                    default -> true; // ignore unknown modes
                };

                if (!match) {
                    fail("Record failed validation for field '%s' using matchMode '%s'. Value was '%s', expected match with '%s'"
                            .formatted(fieldName, matchMode, recordValue, fieldValueNode));
                }
            }
        }
    }

    private boolean containsAny(JsonNode array, String value) {
        for (JsonNode jsonNode : array) {
            if (jsonNode.asText().equals(value)) return true;
        }
        return false;
    }

    private double toDouble(String val) {
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    @Test void testStartsWith() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "asc",
              "filters": [
                {
                  "filedName": "firstName",
                  "filedValue": "Jo",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.STARTS_WITH);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testContains() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "lastName",
                  "filedValue": "Jack",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.CONTAINS);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testContainsAndStartWith() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "lastName",
                  "filedValue": "Jack",
                  "matchMode": "%s"
                },
                {
                  "filedName": "firstName",
                  "filedValue": "Jo",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.CONTAINS,MatchModes.STARTS_WITH);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testNotContains() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "firstName",
                  "filedValue": "John",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.NOT_CONTAINS);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testEndsWith() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "email",
                  "filedValue": ".com",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.ENDS_WITH);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testLessThan() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "id",
                  "filedValue": 5,
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.LESS_THAN);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testLessThanOrEqualTo() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "id",
                  "filedValue": 5,
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.LESS_THAN_OR_EQUAL_TO);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testGreaterThan() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "id",
                  "filedValue": 10,
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.GREATER_THAN);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testGreaterThanOrEqualTo() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "id",
                  "filedValue": 10,
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.GREATER_THAN_OR_EQUAL_TO);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testEquals() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "firstName",
                  "filedValue": "John",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.EQUALS);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testNotEquals() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "firstName",
                  "filedValue": "John",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.NOT_EQUALS);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testIn() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "id",
                  "filedValue": [1,2,3,4],
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.IN);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testNotIn() throws Exception {
        String jsonRequest = """
           {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "id",
                  "filedValue": [1,2,3,4],
                  "matchMode": "%s"
                }
              ]
            }
           """.formatted(MatchModes.NOT_IN);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testDateIs() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "dateOfBirth",
                  "filedValue": "1993-06-25",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.DATE_IS);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testDateIsNot() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "dateOfBirth",
                  "filedValue": "1993-06-25",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.DATE_IS_NOT);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testDateBefore() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "dateOfBirth",
                  "filedValue": "1993-06-25",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.DATE_BEFORE);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testDateAfter() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "dateOfBirth",
                  "filedValue": "1993-06-25",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.DATE_AFTER);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testDateBeforeAndDateAfter() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc",
              "filters": [
                {
                  "filedName": "dateOfBirth",
                  "filedValue": "1994-06-25",
                  "matchMode": "%s"
                },
                {
                  "filedName": "dateOfBirth",
                  "filedValue": "1991-06-25",
                  "matchMode": "%s"
                }
              ]
            }
            """.formatted(MatchModes.DATE_BEFORE ,MatchModes.DATE_AFTER);
        performAndAssertWithFilters(jsonRequest);
    }

    @Test void testNoFilter() throws Exception {
        String jsonRequest = """
            {
              "page": 0,
              "rows": 10,
              "sortField" : "id",
              "sortOrder": "desc"
            }
            """;
        performAndAssertWithoutFilters(jsonRequest);
    }
}
