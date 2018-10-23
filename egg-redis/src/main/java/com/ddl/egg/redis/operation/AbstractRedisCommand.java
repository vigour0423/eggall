package com.ddl.egg.redis.operation;


abstract class AbstractRedisCommand implements RedisCommand {

    protected static final String COMMA = ",";

    private final RedisAccessor redisAccess;

    AbstractRedisCommand(RedisAccessor redisAccess) {
        this.redisAccess = redisAccess;
    }

    @Override
    public <T> T execute(RedisCallBack<T> callBack) {
        return redisAccess.execute(callBack);
    }
}
