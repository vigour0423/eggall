package com.ddl.egg.redis.operation;

import java.util.ArrayList;
import java.util.List;

public final class RedisArgsHelper {

    public static String[] toArray(List<String> values) {
        return values.toArray(new String[values.size()]);
    }

    public static String[] toIntArray(List<Integer> values) {
        List<String> list = toStrList(values);
        return toArray(list);
    }

    private static List<String> toStrList(List<Integer> values) {
        List<String> list = new ArrayList<>();
        for (Integer value : values) {
            if (value == null) {
                continue;
            }
            list.add(String.valueOf(value));
        }
        return list;
    }
}
