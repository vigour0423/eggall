package com.ddl.egg.cache.registry;


@SuppressWarnings("unchecked")
public class CacheRegistryImpl implements CacheRegistry {

    private final CacheCollector cacheCollector;

    public CacheRegistryImpl(CacheCollector cacheCollector) {
        this.cacheCollector = cacheCollector;
    }

    public void addCache(CacheRegistryContext cacheRegistryContext) {
        cacheCollector.addCache(cacheRegistryContext);
    }
}
