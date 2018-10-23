package com.ddl.egg.rest.client.api;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PathVariableProcessor {
    private final Map<String, Object> pathVariableMap = new HashMap<>();

    public String url(final String action) {
        String url = action;
        List<String> searchList = new ArrayList<>();
        List<String> replacementList = new ArrayList<>();
        for (Map.Entry<String, Object> keyValue : pathVariableMap.entrySet()) {
            searchList.add("{" + keyValue.getKey() + "}");
            replacementList.add(keyValue.getValue().toString());
        }
        return StringUtils.replaceEach(url, searchList.toArray(new String[searchList.size()]), replacementList.toArray(new String[replacementList.size()]));
    }

    public Map<String, Object> getPathVariableMap() {
        return pathVariableMap;
    }
}
