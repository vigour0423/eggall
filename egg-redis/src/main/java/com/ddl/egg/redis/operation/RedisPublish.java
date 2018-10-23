package com.ddl.egg.redis.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuyuefan on 2018/3/30.
 */
public class RedisPublish extends AbstractRedisCommand {

    private final Logger logger = LoggerFactory.getLogger(RedisPublish.class);

    public RedisPublish(RedisAccessor redisAccess) {
        super(redisAccess);
    }

    public Long publish(final String channel, final String content) {
        return execute((jedis) -> jedis.publish(channel, content));
    }
}
