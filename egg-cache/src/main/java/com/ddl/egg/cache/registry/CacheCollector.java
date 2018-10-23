package com.ddl.egg.cache.registry;


public interface CacheCollector<T extends CacheRegistryContext> {
    void addCache(T context);
}
