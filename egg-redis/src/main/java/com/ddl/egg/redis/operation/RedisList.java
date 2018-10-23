package com.ddl.egg.redis.operation;

import com.ddl.egg.log.util.TimeLength;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisList extends AbstractRedisCommand {

    private final Logger logger = LoggerFactory.getLogger(RedisList.class);

    public RedisList(RedisAccessor redisAccess) {
        super(redisAccess);
    }

    /**
     * @param key   redis key
     * @param block interpreted as an integer value specifying the maximum number of seconds to block. A timeout of zero can be used to block indefinitely.
     * @return values is  make up of key-value; index=0 is key name, index=1 is value
     * {@code
     * redis-cli> rpush list 1
     * redis-cli> blpop list 10
     * redis-cli> "list"
     * redis-cli> "1"
     * }
     */
    public String leftBlockPop(final String key, final TimeLength block) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("block pop from list left side, key:{}, block-time:{}/s", key, block.toSeconds());
                List<String> values = jedis.blpop((int) block.toSeconds(), key);
                if (CollectionUtils.isEmpty(values) || values.size() != 2) {
                    return null;
                }
                return values.get(1);
            }
        });
    }

    public Long size(final String key) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("get list size key:{}", key);
                return jedis.llen(key);
            }
        });
    }

    /**
     * @param key   redis key
     * @param value remove value
     * @param index index > 0: 从头往尾移除值为 value 的元素。
     *              index < 0: 从尾往头移除值为 value 的元素。
     *              index = 0: 移除所有值为 value 的元素。
     * @return remove count
     */
    public Long remove(final String key, final String value, final int index) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("remove value from list, key:{}, value:{}, index&times:{}", key, value, index);
                return jedis.lrem(key, index, value);
            }
        });
    }

    public Long rightPush(final String key, final String... values) {
        return execute(new RedisCallBack<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                logger.debug("push values to list at right side, key:{}, values:[{}]", key, StringUtils.join(values, COMMA));
                return jedis.rpush(key, values);
            }
        });
    }

    public Long rightPush(final String key, final List<String> values) {
        return rightPush(key, RedisArgsHelper.toArray(values));
    }

    public String leftPop(final String key) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                return jedis.lpop(key);
            }
        });
    }

    public List<String> leftRange(final String key, long start, long stop) {
        return execute(new RedisCallBack<List<String>>() {
            @Override
            public List<String> execute(Jedis jedis) {
                logger.debug("lrange list, key:{}, start:{}, stop:{}", key, start, stop);
                return jedis.lrange(key, start, stop);
            }
        });
    }

    public String leftTrim(final String key, long start, long stop) {
        return execute(new RedisCallBack<String>() {
            @Override
            public String execute(Jedis jedis) {
                logger.debug("ltrim list, key:{}, start:{}, stop:{}", key, start, stop);
                return jedis.ltrim(key, start, stop);
            }
        });
    }
}
