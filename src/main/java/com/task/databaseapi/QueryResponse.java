package com.task.databaseapi;

import java.util.List;
import java.util.Map;

public class QueryResponse {
    private boolean success;
    private List<Map<String, Object>> data;
    private long execution_time_ms;

    public QueryResponse(boolean success, List<Map<String, Object>> data, long executionTimeMs) {
        this.success = success;
        this.data = data;
        this.execution_time_ms = executionTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public long getExecution_time_ms() {
        return execution_time_ms;
    }
}