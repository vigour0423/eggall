package com.ddl.egg.common.id;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lincn on 2018-02-05.
 */
public class IdWorkerFactory {

	private static Map<String, IdWorker> idWorkerMap = new ConcurrentHashMap(16, 0.75f, 16);
	private static ZkMachineIdBuilder zkMachineIdBuilder;

	public IdWorkerFactory(String zkNode, String zkUrl) {
		if (StringUtils.isBlank(zkNode)) {
			throw new IllegalArgumentException("zookeeper节点名称不能为空");
		}
		if (StringUtils.isBlank(zkUrl)) {
			throw new IllegalArgumentException("zookeeper地址不能为空");
		}
		zkMachineIdBuilder = new ZkMachineIdBuilder(zkNode, zkUrl);
		zkMachineIdBuilder.getMachineId();
	}

	/**
	 * 获取IdWorker
	 *
	 * @return
	 */
	public static IdWorker getIdWorker(String cls) {
		IdWorker idWorker = idWorkerMap.get(cls);
		if (idWorker == null) {
			synchronized (IdWorkerFactory.class) {
				if (idWorker == null) {
					idWorker = new IdWorker(zkMachineIdBuilder.getMachineId());
					idWorkerMap.put(cls, idWorker);
				}
			}
		}
		return idWorker;
	}

	public static void clearIdWorker() {
		idWorkerMap.clear();
	}

}
