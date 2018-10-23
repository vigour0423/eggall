package com.ddl.egg.rest.client.api;

import com.ddl.egg.json.JSON;
import com.ddl.egg.log.util.EncodingUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestParamProcessor {

    private final Map<String, Object> requestParamMap = new HashMap<>();
    private int index = 0;

    public String url(final String action, final String encoding) {
        StringBuilder builder = new StringBuilder("?");

        for (Map.Entry<String, Object> keyValue : requestParamMap.entrySet()) {
            boolean isObject = isObject(keyValue.getValue());
            if (!isObject && !StringUtils.hasText(keyValue.getKey()))
                throw new IllegalArgumentException("basic type must set @RequestParam value");

            if (isObject) {
                appendObject(builder, keyValue, encoding);
            } else
                appendSimple(builder, keyValue, encoding);

            index++;
        }
        return action + builder.toString();
    }

    private void appendSimple(StringBuilder builder, Map.Entry<String, Object> keyValue, String encoding) {
        doAppend(builder, keyValue.getKey(), EncodingUtils.url(String.valueOf(keyValue.getValue()), encoding));
    }

    private void appendObject(StringBuilder builder, Map.Entry<String, Object> keyValue, String encoding) {
        Map<String, Object> map = fromObjectToMap(keyValue.getValue());
        for (Map.Entry<String, Object> keyValueObj : map.entrySet()) {
            if (isObject(keyValueObj.getValue()))
                appendObject(builder, keyValueObj, encoding);
            else
                appendSimple(builder, keyValueObj, encoding);

            index++;
        }
    }

    private void doAppend(StringBuilder builder, String key, String value) {
        if (index > 0)
            builder.append('&');
        builder.append(key).append('=').append(value);
    }

    private Map<String, Object> fromObjectToMap(Object object) {
        return JSON.fromJSON(JSON.toJSON(object), Map.class);
    }

    private boolean isObject(Object object) {
        return !ClassUtils.isPrimitiveOrWrapper(object.getClass()) && object.getClass() != String.class;
    }

    public Map<String, Object> getRequestParamMap() {
        return requestParamMap;
    }
}
