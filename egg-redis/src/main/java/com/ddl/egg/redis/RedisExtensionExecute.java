package com.ddl.egg.redis;

import com.ddl.egg.exception.BusinessException;

import java.util.UUID;

/**
 * Created by lincn on 2018/1/10.
 */
public abstract class RedisExtensionExecute {

	private RedisExtension redisExtension;

	public RedisExtensionExecute(RedisExtension redisExtension) {
		this.redisExtension = redisExtension;
	}

	public void execute(String key) {
		String txid = UUID.randomUUID().toString();
		try {
			if (!redisExtension.isNotExists(key, txid)) {
				throw new BusinessException(60201, "消息重复发送");
			}
			doExecute();
		} finally {
			redisExtension.release(key, txid);
		}
	}

	public abstract void doExecute();

	public static void main(String args[]) {
		/*RedisExtensionExecute redisExtensionExecute = new RedisExtensionExecute() {
			@Override
			public void doExecute() {
				System.out.println("aaa");
			}
		};
		redisExtensionExecute.execute("abc");*/
	}

}
