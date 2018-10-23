package com.ddl.egg.cache;

import com.ddl.egg.log.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.Callable;

/**
 * Created by mark.huang on 2016-07-12.
 * 对存入redis的key进行构造cacheName+_+key，防止不同的cacheName，相同的key相互覆盖
 */
public class HKRedisCache extends org.springframework.data.redis.cache.RedisCache {

    private final Logger logger = LoggerFactory.getLogger(HKRedisCache.class);

    public HKRedisCache(String cacheName, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration) {
        super(cacheName, prefix, redisOperations, expiration);
    }

    @Override
    public Cache.ValueWrapper get(Object key) {
        StopWatch watch = new StopWatch();
        boolean hit = false;
        String redisKey = null;
        try {
            redisKey = constructKey(key);
            Cache.ValueWrapper value = super.get(redisKey);
            if (value == null) return null;
            hit = true;
            return value;
        } finally {
            logger.debug("get, cacheName={}, redisKey={}, hit={}, elapsedTime={}", getName(), redisKey, hit, watch.elapsedTime());
        }
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return super.get(key, valueLoader); //todo 这个目前没有发现使用地方，估计是cache sync会用到
    }

    /**
     * @param key
     * @param value 如果value为null不会进行缓存
     */
    @Override
    public void put(Object key, Object value) {
        StopWatch watch = new StopWatch();
        String redisKey = null;
        try {
            redisKey = constructKey(key);
            super.put(redisKey, value);
        } finally {
            logger.debug("put, cacheName={}, redisKey={}, elapsedTime={}", getName(), redisKey, watch.elapsedTime());
        }
    }

    @Override
    public void evict(Object key) {
        StopWatch watch = new StopWatch();
        String redisKey = null;
        try {
            redisKey = constructKey(key);
            super.evict(redisKey);
        } finally {
            logger.debug("evict, cacheName={}, redisKey={}, elapsedTime={}", getName(), redisKey, watch.elapsedTime());
        }
    }

    @Override
    public void clear() {
        StopWatch watch = new StopWatch();
        try {
            super.clear();
        } finally {
            logger.debug("clear, cacheName={}, elapsedTime={}", getName(), watch.elapsedTime());
        }
    }

    private String constructKey(Object key) {
        return getName() + "_" + key.toString();
    }
}
