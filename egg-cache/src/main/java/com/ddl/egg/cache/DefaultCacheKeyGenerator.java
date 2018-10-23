package com.ddl.egg.cache;

import com.ddl.egg.log.util.ConvertUtil;
import com.ddl.egg.log.util.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Date;


public class DefaultCacheKeyGenerator implements KeyGenerator {
    private final Logger logger = LoggerFactory.getLogger(DefaultCacheKeyGenerator.class);
    static final String NO_PARAM_KEY = "default";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String key;

        if (params.length == 0) {
            key = NO_PARAM_KEY;
        } else {
            key = buildStringCacheKey(params);
        }
        String encodedKey = encodeKey(key);
        logger.debug("cache key, method={}, key={}, encodedKey={}", method, key, encodedKey);
        return encodedKey;
    }

    public String encodeKey(String key) {
        // for long key or illegal key, use md5 to short
        if (key.length() > 32 || containsIllegalKeyChar(key)) {
            return DigestUtils.md5(key);
        }
        return key;
    }

    private String buildStringCacheKey(Object[] params) {
        if (params.length == 1)
            return getKeyValue(params[0]);

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Object param : params) {
            if (index > 0)
                builder.append(':');
            String value = getKeyValue(param);
            builder.append(value);
            index++;
        }
        return builder.toString();
    }

    private String getKeyValue(Object param) {
        if (param instanceof CacheKeyGenerator)
            return ((CacheKeyGenerator) param).buildCacheKey();
        if (param instanceof Enum)
            return ((Enum) param).name();
        if (param instanceof Date)
            return ConvertUtil.toString((Date) param, ConvertUtil.DATE_FORMAT_DATETIME);
        return String.valueOf(param);
    }

    private boolean containsIllegalKeyChar(String value) {
        return value.contains(" ");
    }
}
