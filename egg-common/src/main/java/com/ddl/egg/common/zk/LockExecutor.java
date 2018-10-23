package com.ddl.egg.common.zk;

import com.google.common.collect.Maps;
import com.ddl.egg.common.spring.SpringContextHolder;
import com.ddl.egg.common.zk.construction.LockNameSpaceCuratorClientManager;
import com.ddl.egg.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created lincn on 2018/4/2.
 */
public class LockExecutor {

	private Map<String, InterProcessMutex> lockerMap = Maps.newConcurrentMap();

	private InterProcessMutex getLocker(String key) {
		InterProcessMutex locker = lockerMap.get(key);
		if (locker == null) {
			synchronized (this) {
				locker = lockerMap.get(key);
				if (locker == null) {
					if (StringUtils.isBlank(key)) {
						throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "key不能为空");
					}
					LockNameSpaceCuratorClientManager lockNameSpaceCuratorClient = SpringContextHolder.getBean(LockNameSpaceCuratorClientManager.class);
					locker = new InterProcessMutex(lockNameSpaceCuratorClient.getClient(), "/" + key);
					lockerMap.put(key, locker);
				}
			}
		}
		return locker;
	}

	public void lock(String key, long seconds) {
		try {
			if (!getLocker(key).acquire(seconds, TimeUnit.SECONDS)) {
				throw new BusinessException(HttpStatus.SC_GATEWAY_TIMEOUT, "获取锁超时");
			}
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "加锁失败", e);
		}
	}

	public void lock(String key) {
		try {
			getLocker(key).acquire();
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "加锁失败", e);
		}
	}

	public void release(String key) {
		try {
			if (getLocker(key).isAcquiredInThisProcess()) {
				getLocker(key).release();
			}
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "锁释放失败", e);
		}
	}
}
