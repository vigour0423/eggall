package com.ddl.egg.common.spring.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ResultSets {
    private static final String RESULT_KEY_RETURN_VALUE = "RETURN_VALUE";
    private static final String RESULT_KEY_UPDATE_COUNT = "update-count";

    private final Logger logger = LoggerFactory.getLogger(StoreProcedure.class);
    private final Map<String, Object> results;

    public ResultSets(Map<String, Object> results) {
        this.results = results;
        logResults(results);
    }

    private void logResults(Map<String, Object> results) {
        logger.debug("results, totalResultSets={}", results.size());
        for (Entry<String, Object> entry : results.entrySet()) {
            int count = 1;
            Object value = entry.getValue();
            if (value instanceof List)
                count = ((List) value).size();
            logger.debug("results, name={}, count={}", entry.getKey(), count);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getResultSet(int index) {
        List<T> resultSet = (List<T>) results.get(StoreProcedure.buildResultSetIndex(index));
        Assert.notNull(resultSet, "sp didn't return expected resultSet, index=" + index);
        return resultSet;
    }

    @SuppressWarnings("unchecked")
    public <T> T getUniqueResult(int index) {
        List<Object> resultSet = getResultSet(index);
        if (resultSet.size() > 1)
            throw new IncorrectResultSizeDataAccessException(1, resultSet.size());
        if (resultSet.isEmpty())
            return null;
        return (T) resultSet.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOutParamValue(String param) {
        return (T) results.get(param);
    }

    public int getReturnValue() {
        return (Integer) results.get(RESULT_KEY_RETURN_VALUE);
    }

    public int getTotalUpdateCount() {
        int count = 0;

        for (Entry<String, Object> entry : results.entrySet()) {
            if (entry.getKey().contains(RESULT_KEY_UPDATE_COUNT)) {
                count += (Integer) entry.getValue();
            }
        }

        return count;
    }
}
