package com.ddl.egg.redis.operation;


interface RedisCommand {

    <T> T execute(RedisCallBack<T> callBack);
}
