package com.ddl.egg.redis.operation;

import com.ddl.egg.json.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisHash extends AbstractRedisCommand {

    private final Logger logger = LoggerFactory.getLogger(RedisHash.class);

    public RedisHash(RedisAccessor redisAccess) {
        super(redisAccess);
    }

    public Long decrement(final String key, final String hashKey, final long delta) {
        Assert.isTrue(delta >= 0, "delta must be gte 0");
        return increment(key, hashKey, delta * -1);
    }

    public Long increment(final String key, final String hashKey, final long delta) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("incr hash value, key:{}, hashKey:{}, delta:{}", key, hashKey, delta);
                Long value = jedis.hincrBy(key, hashKey, delta);
                logger.debug("key:{}, hash-field:{}, current-value:{}", key);
                return value;
            }
        });
    }

    public String get(final String key, final String hashKey) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("get value from hash, key:{}, hashKey:{}", key, hashKey);
                return jedis.hget(key, hashKey);
            }
        });
    }

    public List<String> multiGet(final String key, final String... hashKeys) {
        return execute(new RedisCallBack<List<String>>() {
            @Override
            public List<String> execute(Jedis jedis) {
                logger.debug("multi get values from hash, key:{}, hash-keys:[{}]", key, StringUtils.join(hashKeys, COMMA));
                return jedis.hmget(key, hashKeys);
            }
        });
    }

    public List<String> multiGet(final String key, final List<String> hashKeys) {
        return multiGet(key, RedisArgsHelper.toArray(hashKeys));
    }

    public String putAll(final String key, final Map<String, String> hash) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                String response = jedis.hmset(key, hash);
                logger.debug("multi put hash, key:{}, hash:[{}], response:{}", key, JSON.toJSON(hash), response);
                return response;
            }
        });
    }

    public long remove(final String key, final String hashKey) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("remove hash-field:{} from hash-key:{}", hashKey, key);
                return jedis.hdel(key, hashKey);
            }
        });
    }

    public Long put(final String key, final String hashKey, final String value) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("put hash-value to hash, key:{}, hash-field:{}, value:{}", key, hashKey, value);
                return jedis.hset(key, hashKey, value);
            }
        });
    }

    public Set<String> hashKeys(final String key) {
        return execute(new RedisCallBack<Set<String>>() {
            @Override
            public Set<String> execute(Jedis jedis) {
                logger.debug("list hash-field to hash, key:{}", key);
                return jedis.hkeys(key);
            }
        });
    }
}
