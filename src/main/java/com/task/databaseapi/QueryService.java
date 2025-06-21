package com.task.databaseapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryService {

    @Autowired
    private DataSource dataSource;

    public QueryResponse runQuery(String identifier) throws SQLException, IOException {
        String fileName = "queries/" + identifier + ".sql";
        File file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("Query file not found");
        }

        String query = Files.readString(file.toPath());

        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> resultData = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                resultData.add(row);
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;

        return new QueryResponse(true, resultData, executionTime);
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
