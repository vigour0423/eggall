package com.ddl.egg.cache;


public class CacheSettings {

    private CacheProvider cacheProvider = CacheProvider.EHCACHE;
    /**
     * The value cachePrefix only support redis now
     */
    private String cachePrefix;

    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }
}
