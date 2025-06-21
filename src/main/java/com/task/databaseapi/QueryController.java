package com.task.databaseapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/execute-query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<?> executeQuery(@RequestParam String query_identifier) {
        try {
            QueryResponse response = queryService.runQuery(query_identifier);
            return ResponseEntity.ok(response);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("success", false, "error", e.getMessage(), "code", 404)
            );
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("success", false, "error", e.getMessage(), "code", 500)
            );
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("success", false, "error", "Failed to read query file", "code", 500)
            );
        }
    }
}

