package com.ddl.egg.redis.operation;

import com.ddl.egg.log.util.TimeLength;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RedisKeys extends AbstractRedisCommand {

    private final Logger logger = LoggerFactory.getLogger(RedisKeys.class);

    public RedisKeys(RedisAccessor redisAccess) {
        super(redisAccess);
    }

    public Set<String> keys(final String pattern) {
        return execute(new RedisCallBack<Set<String>>() {
            @Override
            public Set<String> execute(Jedis jedis) {
                return jedis.keys(pattern);
            }
        });
    }

    public Long expire(final String key, final TimeLength expire) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("set expire time, key:{}, expire-time:{}/s", key, expire.toSeconds());
                return jedis.expire(key, (int) expire.toSeconds());
            }
        });
    }

    public Long delete(final String... keys) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("del keys:{}", StringUtils.join(keys, COMMA));
                return jedis.del(keys);
            }
        });
    }

    public Boolean exists(final String key) {
        return execute(new RedisCallBack<Boolean>() {
            @Override
            public Boolean execute(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }
}
