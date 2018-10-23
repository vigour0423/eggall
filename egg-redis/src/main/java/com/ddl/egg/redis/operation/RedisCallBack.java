package com.ddl.egg.redis.operation;

import redis.clients.jedis.Jedis;

public interface RedisCallBack<T> {
    T execute(Jedis jedis);
}
