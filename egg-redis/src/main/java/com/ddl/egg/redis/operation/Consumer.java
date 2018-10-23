package com.ddl.egg.redis.operation;


public interface Consumer<T> {

    void execute(T t);
}
