package com.ddl.egg.cache.registry;


import com.ddl.egg.log.util.TimeLength;

public class CacheRegistryContext {

    private String cacheName;
    private TimeLength expirationTime;
    private int maxEntriesInHeap;
    private Class<?> objectType;

    public CacheRegistryContext() {
    }

    public CacheRegistryContext(String cacheName, TimeLength expirationTime) {
        this.cacheName = cacheName;
        this.expirationTime = expirationTime;
    }

    public CacheRegistryContext(String cacheName, TimeLength expirationTime, int maxEntriesInHeap) {
        this.cacheName = cacheName;
        this.expirationTime = expirationTime;
        this.maxEntriesInHeap = maxEntriesInHeap;
    }

    public CacheRegistryContext(String cacheName, TimeLength expirationTime, Class<?> objectType) {
        this.cacheName = cacheName;
        this.expirationTime = expirationTime;
        this.objectType = objectType;
    }

    public CacheRegistryContext(String cacheName, TimeLength expirationTime, int maxEntriesInHeap, Class<?> objectType) {
        this.cacheName = cacheName;
        this.expirationTime = expirationTime;
        this.maxEntriesInHeap = maxEntriesInHeap;
        this.objectType = objectType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public TimeLength getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(TimeLength expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getMaxEntriesInHeap() {
        return maxEntriesInHeap;
    }

    public void setMaxEntriesInHeap(int maxEntriesInHeap) {
        this.maxEntriesInHeap = maxEntriesInHeap;
    }

    public Class<?> getObjectType() {
        return objectType;
    }

    public void setObjectType(Class<?> objectType) {
        this.objectType = objectType;
    }
}
