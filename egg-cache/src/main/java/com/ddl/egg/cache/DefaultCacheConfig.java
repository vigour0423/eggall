package com.ddl.egg.cache;

import com.ddl.egg.cache.registry.CacheCollector;
import com.ddl.egg.cache.registry.CacheRegistry;
import com.ddl.egg.cache.registry.CacheRegistryImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.core.RedisTemplate;

@EnableCaching(proxyTargetClass = true)
public abstract class DefaultCacheConfig implements CachingConfigurer {

    static {
        System.setProperty("net.sf.ehcache.skipUpdateCheck", "true");//close ehcache update check schedule
    }

    public static final String REDIS_TEMPLATE_NAME = "redisTemplateCache";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public CacheSettings cacheSettings() {
        return new CacheSettings();
    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = createCacheManager();
        addCaches(new CacheRegistryImpl((CacheCollector) cacheManager));
        return cacheManager;
    }

    protected CacheManager createCacheManager() {
        CacheSettings settings = cacheSettings();
        CacheProvider provider = settings.getCacheProvider();
        switch (provider) {
            case EHCACHE:
                return new EhcacheCacheManager();
            case REDIS:
                return redisCacheManager(settings.getCachePrefix());
            default:
                throw new IllegalStateException("not supported cache provider, provider=" + provider);
        }
    }

    private HKRedisCacheManager redisCacheManager(String prefix) {
        RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean(REDIS_TEMPLATE_NAME);
        HKRedisCacheManager redisCacheManager = new HKRedisCacheManager(redisTemplate);
        if (StringUtils.isNotBlank(prefix)) {
            redisCacheManager.setUsePrefix(true);
            redisCacheManager.setCachePrefix(new DefaultRedisCachePrefix(prefix));
        }
        return redisCacheManager;
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new DefaultCacheKeyGenerator();
    }

    protected abstract void addCaches(CacheRegistry registry);
}
