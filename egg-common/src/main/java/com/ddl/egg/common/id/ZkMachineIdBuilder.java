package com.ddl.egg.common.id;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * zookeeper机器id生成器
 *
 * @author lincn
 */

public class ZkMachineIdBuilder implements Watcher {
	private static final Logger logger = LoggerFactory.getLogger(ZkMachineIdBuilder.class);

	private final int sessionTimeout = 10000;
	private final String zkPath = "/idWorker";
	private int nodeId = -1;
	private String zkNode;
	private String url;
	private ZooKeeper zooKeeper;

	public ZkMachineIdBuilder(String zkNode, String url) {
		this.zkNode = zkNode;
		this.url = url;
		try {
			this.zooKeeper = new ZooKeeper(url, sessionTimeout, this);
		} catch (IOException e) {
			logger.error("连接Zookeeper失败");
		}
	}
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDeleted) {
			nodeId = -1;
			IdWorkerFactory.clearIdWorker();
			logger.info("ZkMachineIdBuilder机器节点被删除：" + event.getPath());
		}
	}

	/**
	 * 创建128个节点
	 *
	 * @throws InterruptedException
	 * @throws org.apache.zookeeper.KeeperException
	 * @throws java.io.IOException
	 */
	public void create64Node() throws KeeperException, InterruptedException, IOException {
		createNodePersistent(zkPath);
		createNodePersistent(zkPath + zkNode);
		for (int i = 0; i < 64; i++) {
			String strInt = String.valueOf(i);
			if (i < 10) {
				strInt = "0" + strInt;
			}
			createNodePersistent(zkPath + zkNode + "/" + strInt);
		}
	}

	/**
	 * 创建永久节点
	 *
	 * @param path
	 * @throws org.apache.zookeeper.KeeperException
	 * @throws InterruptedException
	 */
	public void createNodePersistent(String path) throws KeeperException, InterruptedException {
		Stat stat = zooKeeper.exists(path, false);
		if (stat == null) {
			zooKeeper.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
	}

	/**
	 * 获取机器id
	 *
	 * @return
	 * @throws java.io.IOException
	 * @throws org.apache.zookeeper.KeeperException
	 * @throws InterruptedException
	 */
	public synchronized Integer getMachineId() {
		try {
			if (nodeId == -1) {
				Stat stat = zooKeeper.exists(zkPath + zkNode, false);
				if (stat == null) {
					create64Node();
				}
				List<String> idNodes = zooKeeper.getChildren(zkPath + zkNode, false);
				if (idNodes != null && idNodes.size() > 0) {
					Collections.sort(idNodes);
					for (String idNode : idNodes) {
						String idPath = zkPath + zkNode + "/" + idNode;
						Stat statN = zooKeeper.exists(idPath + "/node", false);
						if (statN == null) {
							try {
								zooKeeper.create(idPath + "/node", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
								zooKeeper.exists(idPath + "/node", this);
								nodeId = Integer.parseInt(idNode);
								break;
							} catch (Exception e) {
								logger.error("从zookeeper获取machineId异常");
							}
						}
					}
				}
				if (nodeId < 0) {
					throw new RuntimeException("生成机器id失败");
				}
			}
		} catch (Exception e) {
			logger.error("从zookeeper获取machineId异常");
			throw new RuntimeException(e);
		}
		return nodeId;
	}

}
