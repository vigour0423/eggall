package com.ddl.egg.cache;

import com.ddl.egg.cache.registry.CacheCollector;
import com.ddl.egg.cache.registry.CacheRegistryContext;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class EhcacheCacheManager extends EhCacheCacheManager implements CacheCollector {
    public EhcacheCacheManager() {
        setCacheManager(new net.sf.ehcache.CacheManager());
    }

    public void addCache(Ehcache cache) {
        getCacheManager().addCache(cache);
    }

    @Override
    protected Collection<Cache> loadCaches() {
        CacheManager cacheManager = getCacheManager();

        String[] names = cacheManager.getCacheNames();
        List<Cache> caches = new ArrayList<>(names.length);
        for (String name : names) {
            caches.add(new EhcacheCache(cacheManager.getEhcache(name)));
        }
        return caches;
    }

    @Override
    public void addCache(CacheRegistryContext context) {
        Assert.hasText(context.getCacheName(), "cache name must be not null.");
        Assert.notNull(context.getExpirationTime(), "expire time must be not null.");
        CacheConfiguration cacheConfiguration = new CacheConfiguration(context.getCacheName(), context.getMaxEntriesInHeap());
        cacheConfiguration.setTimeToLiveSeconds(context.getExpirationTime().toSeconds());
        addCache(new net.sf.ehcache.Cache(cacheConfiguration));
    }
}
