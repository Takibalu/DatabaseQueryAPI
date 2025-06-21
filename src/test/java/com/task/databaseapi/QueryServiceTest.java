package com.task.databaseapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceTest {

    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private ResultSetMetaData mockMetaData;

    private QueryService queryService;

    @BeforeEach
    void setup() throws Exception {
        mockDataSource = mock(DataSource.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockMetaData = mock(ResultSetMetaData.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getMetaData()).thenReturn(mockMetaData);

        when(mockMetaData.getColumnCount()).thenReturn(2);
        when(mockMetaData.getColumnName(1)).thenReturn("id");
        when(mockMetaData.getColumnName(2)).thenReturn("name");

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getObject(1)).thenReturn(1);
        when(mockResultSet.getObject(2)).thenReturn("Alice");

        queryService = new QueryService();
        queryService.setDataSource(mockDataSource);


        File queriesDir = new File("queries");
        queriesDir.mkdir();
        try (FileWriter writer = new FileWriter("queries/test_query.sql")) {
            writer.write("SELECT id, name FROM users;");
        }
    }

    @Test
    void runQuery_returnsExpectedData() throws Exception {
        QueryResponse response = queryService.runQuery("test_query");

        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
        Map<String, Object> row = response.getData().get(0);
        assertEquals(1, row.get("id"));
        assertEquals("Alice", row.get("name"));
        assertTrue(response.getExecution_time_ms() >= 0);
    }
}
