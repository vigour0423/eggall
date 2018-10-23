package com.ddl.egg.redis.operation;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

public interface RedisAccessor {

    <T> T execute(RedisCallBack<T> callBack);

    void pipelined(Consumer<Pipeline> consumer);

    void transaction(Consumer<Transaction> consumer);
}
