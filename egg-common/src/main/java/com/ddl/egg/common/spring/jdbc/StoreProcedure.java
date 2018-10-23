package com.ddl.egg.common.spring.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class StoreProcedure {
    private final Logger logger = LoggerFactory.getLogger(StoreProcedure.class);

    static String buildResultSetIndex(int index) {
        return "result-set-" + index;
    }

    private final String name;
    private final Map<String, RowMapper> rowMappers = new LinkedHashMap<>();
    private final Map<String, Object> params = new HashMap<>();
    private int resultSetIndex = 0;

    public StoreProcedure(String name) {
        this.name = name;
    }

    public StoreProcedure addRowMapper(RowMapper rowMapper) {
        rowMappers.put(buildResultSetIndex(resultSetIndex), rowMapper);
        resultSetIndex++;
        return this;
    }

    public StoreProcedure addOutParamRowMapper(String param, RowMapper rowMapper) {
        rowMappers.put(param.toLowerCase(), rowMapper);
        return this;
    }

    public StoreProcedure setParam(String param, Object value) {
        params.put(param, value);
        return this;
    }

    ResultSets execute(JdbcTemplate jdbcTemplate) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(name)
                .withReturnValue();

        for (Map.Entry<String, RowMapper> entry : rowMappers.entrySet()) {
            jdbcCall.returningResultSet(entry.getKey(), entry.getValue());
        }
        Map<String, Object> results = jdbcCall.execute(params);
        logger.debug("execute jdbc call, sp={}, params={}", name, params);
        return new ResultSets(results);
    }

    String getName() {
        return name;
    }
}
