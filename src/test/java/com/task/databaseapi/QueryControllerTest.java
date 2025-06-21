package com.task.databaseapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QueryController.class)
public class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @Test
    void shouldReturn200WithData() throws Exception {
        QueryResponse mockResponse = new QueryResponse(true,
                List.of(Map.of("id", 1, "name", "Alice")), 15L);

        when(queryService.runQuery("get_user_data")).thenReturn(mockResponse);

        mockMvc.perform(get("/execute-query")
                        .param("query_identifier", "get_user_data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Alice"));
    }

    @Test
    void shouldReturn404IfFileNotFound() throws Exception {
        when(queryService.runQuery("missing_query"))
                .thenThrow(new FileNotFoundException("Query file not found"));

        mockMvc.perform(get("/execute-query")
                        .param("query_identifier", "missing_query"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Query file not found"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void shouldReturn500IfSqlFails() throws Exception {
        when(queryService.runQuery("bad_query"))
                .thenThrow(new SQLException("Syntax error"));

        mockMvc.perform(get("/execute-query")
                        .param("query_identifier", "bad_query"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Syntax error"))
                .andExpect(jsonPath("$.code").value(500));
    }
}
