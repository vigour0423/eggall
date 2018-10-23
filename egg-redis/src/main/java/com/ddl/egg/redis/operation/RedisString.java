package com.ddl.egg.redis.operation;

import com.ddl.egg.log.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisString extends AbstractRedisCommand {

    private final Logger logger = LoggerFactory.getLogger(RedisString.class);

    public RedisString(RedisAccessor redisAccess) {
        super(redisAccess);
    }

    public String set(final String key, final String value) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("set key:{}, value:{}", key, value);
                return jedis.set(key, value);
            }
        });
    }

    public Long multiSetIfAbsent(final String... keyValues) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.msetnx(keyValues);
            }
        });
    }

    public Long increment(final String key, final long delta) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("incrBy key:{}, delta:{}", key, delta);
                Long value = jedis.incrBy(key, delta);
                logger.debug("after incr value:{}", value);
                return value;
            }
        });
    }

    public Long decrement(final String key, final long delta) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("decrBy key:{}, delta:{}", key, delta);
                Long value = jedis.decrBy(key, delta);
                logger.debug("after decr value:{}", value);
                return value;
            }
        });
    }

    public String get(final String key) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("get key:{}", key);
                String value = jedis.get(key);
                logger.debug("get value:{}", value);
                return value;
            }
        });
    }

    public List<String> multiGet(final String... keys) {
        return execute(new RedisCallBack<List<String>>() {
            @Override
            public List<String> execute(Jedis jedis) {
                return jedis.mget(keys);
            }
        });
    }

    public Long getInt(final String key) {
        return increment(key, 0);
    }

    public String setValueWithExpireTime(final String key, final String value, final TimeLength expireTime) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("set key:{}, value:{}, expireTime:{}", key, value, expireTime.toMilliseconds());
                return jedis.setex(key, (int) expireTime.toSeconds(), value);
            }
        });
    }

    public String setValueWithNXPX(final String key, final String value, final TimeLength expireTime) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("set key:{}, value:{}, expireTime:{}", key, value, expireTime.toMilliseconds());
                return jedis.set(key, value, "NX", "PX", expireTime.toMilliseconds());
            }
        });
    }
}
