package com.ddl.egg.redis;

import com.ddl.egg.log.util.TimeLength;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Created by mark.huang on 2016-06-17.
 */
public abstract class DefaultRedisConfig {

    public abstract RedisConfigMeta configMeta();

    @Bean
    public RedisAccess redisAccess() {
        return new RedisAccess(jedisPool());
    }

    private JedisPool jedisPool() {
        String password = configMeta().getPassword();
        if (StringUtils.hasText(password)) {
            return new JedisPool(jedisPoolConfig(), configMeta().getHost(), configMeta().getPort(), 5000, password);
        } else {
            return new JedisPool(jedisPoolConfig(), configMeta().getHost(), configMeta().getPort(), 5000);
        }
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMaxTotal(50);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(TimeLength.minutes(5).toMilliseconds());
        jedisPoolConfig.setMaxWaitMillis(2000);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }
}
