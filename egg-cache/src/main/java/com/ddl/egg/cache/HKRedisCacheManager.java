package com.ddl.egg.cache;

import com.ddl.egg.cache.registry.CacheCollector;
import com.ddl.egg.cache.registry.CacheRegistryContext;
import com.ddl.egg.log.util.TimeLength;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class HKRedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager implements CacheCollector {

    private final List<HKRedisCache> caches = new ArrayList<>();


    public HKRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    public HKRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    @Override
    public void addCache(CacheRegistryContext context) {
        Assert.hasText(context.getCacheName(), "cache name must be not null.");
        Assert.notNull(context.getExpirationTime(), "expire time must be not null.");
        add(context.getCacheName(), context.getObjectType(), context.getExpirationTime());
    }

    public void add(String cacheName, Class<?> objectType, TimeLength expirationTime) {
        caches.add(new HKRedisCache(cacheName, (isUsePrefix() ? getCachePrefix().prefix(cacheName) : null), getRedisOperations(), expirationTime.toSeconds()));
    }
}
