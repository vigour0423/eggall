package com.ddl.egg.mongo.util;

import com.ddl.egg.mongo.MongoConstants;
import org.springframework.data.mongodb.core.query.Query;


public class MongoUtils {

    public static void addIncludeKeys(Query query, String[] includeKeys) {
        if (null != includeKeys && includeKeys.length > 0) {
            for (String key : includeKeys) {
                query.fields().include(key);
            }
        }
    }

    public static String toLike(String value) {
        if (value == null) return null;
        return MongoConstants.MONGO_LIKE.replace("value", value);
    }
}
