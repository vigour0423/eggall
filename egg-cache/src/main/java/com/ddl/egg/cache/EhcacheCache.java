package com.ddl.egg.cache;

import com.ddl.egg.log.util.StopWatch;
import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;


public class EhcacheCache extends EhCacheCache {
    private final Logger logger = LoggerFactory.getLogger(EhcacheCache.class);

    public EhcacheCache(Ehcache ehcache) {
        super(ehcache);
    }

    @Override
    public Cache.ValueWrapper get(Object key) {
        StopWatch watch = new StopWatch();
        boolean hit = false;
        try {
            Cache.ValueWrapper value = super.get(key);
            if (value == null) return null;
            hit = true;
            return value;
        } finally {
            logger.debug("get, key={}, hit={}, elapsedTime={}", key, hit, watch.elapsedTime());
        }
    }

    @Override
    public void put(Object key, Object value) {
        StopWatch watch = new StopWatch();
        try {
            super.put(key, value);
        } finally {
            logger.debug("put, key={}, elapsedTime={}", key, watch.elapsedTime());
        }
    }

    @Override
    public void evict(Object key) {
        StopWatch watch = new StopWatch();
        try {
            super.evict(key);
        } finally {
            logger.debug("evict, key={}, elapsedTime={}", key, watch.elapsedTime());
        }
    }

    @Override
    public void clear() {
        super.clear();
    }

}
