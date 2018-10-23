package com.ddl.egg.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lincn on 2018/1/10.
 */
public class RedisExtension {

	private static final Logger logger = LoggerFactory.getLogger(RedisExtension.class);

	private final JedisPool redisPool;

	public RedisExtension(JedisPool redisPool) {
		this.redisPool = redisPool;
	}

	public boolean isNotExists(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = redisPool.getResource();
			String res = jedis.set(key, value, "NX", "EX", 30);
			return StringUtils.equalsIgnoreCase("OK", res);
		} catch (Exception e) {
			logger.error("RedisExtension 调用redis出现异常", e);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public void release(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = redisPool.getResource();
			String onlineValue = jedis.get(key);
			if (onlineValue == null ? value == null : onlineValue.equals(value)) {
				jedis.del(key);
			}
		} catch (Exception e) {
			logger.error("RedisExtension 调用redis出现异常", e);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 返还到连接池
	 *
	 * @param jedis
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
