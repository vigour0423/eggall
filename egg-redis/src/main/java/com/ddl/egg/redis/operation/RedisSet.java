package com.ddl.egg.redis.operation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class RedisSet extends AbstractRedisCommand {

    private final Logger logger = LoggerFactory.getLogger(RedisSet.class);

    public RedisSet(RedisAccessor redisAccess) {
        super(redisAccess);
    }

    public String pop(final String key) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("pop value from set, key:{}", key);
                return jedis.spop(key);
            }
        });
    }

    public Long push(final String key, final List<String> values) {
        return push(key, values.toArray(new String[values.size()]));
    }

    public Long push(final String key, final String... values) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("add values to set, key:{} values:[{}]", key, StringUtils.join(values, COMMA));
                return jedis.sadd(key, values);
            }
        });
    }

    public Long size(final String key) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("get set size, key:{}", key);
                return jedis.scard(key);
            }
        });
    }

    public Set<String> members(final String key) {
        return execute(new RedisCallBack<Set<String>>() {
            @Override
            public Set<String> execute(Jedis jedis) {
                logger.debug("list set members key:{}", key);
                return jedis.smembers(key);
            }
        });
    }

    public Long remove(final String key, final List<String> values) {
        return remove(key, RedisArgsHelper.toArray(values));
    }

    public Long remove(final String key, final String... values) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("remove set keys, key:{} values:[{}]", key, StringUtils.join(values, COMMA));
                return jedis.srem(key, values);
            }
        });
    }
}
