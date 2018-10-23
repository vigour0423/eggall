package com.ddl.egg.common.zk.construction;

/**
 * Created by lincn on 2018/3/21.
 */
public class LockNameSpaceCuratorClientManager extends NameSpaceCuratorClientManager {

	public LockNameSpaceCuratorClientManager(String serverLists, int baseSleepTimeMs, int maxRetries, int maxSleepMs) {
		super("lock",serverLists, baseSleepTimeMs, maxRetries, maxSleepMs);
	}

}
