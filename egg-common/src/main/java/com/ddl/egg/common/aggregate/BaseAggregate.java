package com.ddl.egg.common.aggregate;

import com.ddl.egg.common.spring.SpringContextHolder;
import com.ddl.egg.common.zk.construction.LockNameSpaceCuratorClientManager;
import com.ddl.egg.exception.BusinessException;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.http.HttpStatus;

import java.util.concurrent.TimeUnit;

/**
 * Created by lincn on 2018/3/20.
 */
public abstract class BaseAggregate {

	private InterProcessMutex locker;

	public InterProcessMutex getLocker() {
		if (locker == null) {
			synchronized (this) {
				if (locker == null) {
					if (getId() == null) {
						throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "聚合根主键不能为空");
					}
					LockNameSpaceCuratorClientManager lockNameSpaceCuratorClient = SpringContextHolder.getBean(LockNameSpaceCuratorClientManager.class);
					locker = new InterProcessMutex(lockNameSpaceCuratorClient.getClient(), this.getClass().getName() + "/" + getId());
				}
			}
		}
		return locker;
	}

	public void lock() {
		lock(180L);
	}

	public void lock(long seconds) {
		try {
			if (!getLocker().acquire(seconds, TimeUnit.SECONDS)) {
				throw new BusinessException(HttpStatus.SC_GATEWAY_TIMEOUT, "获取锁超时");
			}
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "加锁失败", e);
		}
	}

	public void release() {
		try {
			if (getLocker().isAcquiredInThisProcess()) {
				getLocker().release();
			}
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "锁释放失败", e);
		}
	}

	public abstract Object getId();

}
