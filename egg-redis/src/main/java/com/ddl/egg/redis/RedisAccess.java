package com.ddl.egg.redis;

import com.ddl.egg.log.util.StopWatch;
import com.ddl.egg.redis.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;

public class RedisAccess implements RedisAccessor {

    private final Logger logger = LoggerFactory.getLogger(RedisAccess.class);

    private final JedisPool redisPool;

    public RedisAccess(JedisPool redisPool) {
        this.redisPool = redisPool;
        initialOps();
    }

    public <T> T execute(RedisCallBack<T> callBack) {
        StopWatch stopWatch = new StopWatch();
        try (Jedis resource = getRedisPool().getResource()) {
            return callBack.execute(resource);
        } finally {
            logger.debug("execute redis command elapsedTime={}(ms)", stopWatch.elapsedTime());
        }
    }

    public void pipelined(Consumer<Pipeline> consumer) {
        StopWatch stopWatch = new StopWatch();
        try (Jedis resource = getRedisPool().getResource(); Pipeline pipelined = resource.pipelined()) {
            consumer.execute(pipelined);
            pipelined.sync();
        } catch (IOException e) {
            throw new JedisException("Could not return the resource to the pool", e);
        } finally {
            logger.debug("exec pipeline, elapsedTime={}(ms)", stopWatch.elapsedTime());
        }
    }

    public void transaction(Consumer<Transaction> consumer) {
        StopWatch stopWatch = new StopWatch();
        try (Jedis jedis = getRedisPool().getResource(); Transaction transaction = jedis.multi()) {
            consumer.execute(transaction);
            transaction.exec();
        } catch (IOException e) {
            throw new JedisException("Could not return the resource to the pool", e);
        } finally {
            logger.debug("exec transaction, elapsedTime={}(ms)", stopWatch.elapsedTime());
        }
    }

    public Object eval(String script, int keyCount, String... params) {
        StopWatch stopWatch = new StopWatch();
        try (Jedis jedis = getRedisPool().getResource()) {
            return jedis.eval(script, keyCount, params);
        } finally {
            logger.debug("exec eval, elapsedTime={}(ms)", stopWatch.elapsedTime());
        }
    }

    public RedisKeys keyOps() {
        return redisKeys;
    }

    public RedisString stringOps() {
        return redisString;
    }

    public RedisHash hashOps() {
        return redisHash;
    }

    public RedisList listOps() {
        return redisList;
    }

    public RedisSet setOps() {
        return redisSet;
    }

    public RedisPublish publishOps() {
        return redisPublish;
    }

    public JedisPool getRedisPool() {
        return redisPool;
    }

    private RedisKeys redisKeys;
    private RedisString redisString;
    private RedisHash redisHash;
    private RedisList redisList;
    private RedisSet redisSet;
    private RedisPublish redisPublish;

    private void initialOps() {
        redisKeys = new RedisKeys(this);
        redisString = new RedisString(this);
        redisHash = new RedisHash(this);
        redisList = new RedisList(this);
        redisSet = new RedisSet(this);
        redisPublish = new RedisPublish(this);
    }
}
