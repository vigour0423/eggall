package com.ddl.egg.common.zk.construction;

import com.google.common.collect.Maps;
import com.ddl.egg.exception.BusinessException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.http.HttpStatus;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lincn on 2018/3/21.
 */
public class NameSpaceCuratorClientManager {

	private static Logger logger = LoggerFactory.getLogger(NameSpaceCuratorClientManager.class);

	Map<String, Closeable> caches = Maps.newConcurrentMap();

	protected CuratorFramework client;

	protected final String errMsg = "zookeeper操作异常";

	public NameSpaceCuratorClientManager(String namespace, String serverLists, int baseSleepTimeMs, int maxRetries, int maxSleepMs) {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
		                                                                 .connectString(serverLists)
		                                                                 .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, maxSleepMs))
		                                                                 .namespace(namespace);
		client = builder.build();

	}

	public NameSpaceCuratorClientManager(CuratorFramework client) {
		this.client = client;
	}

	public boolean isExists(String path) {
		try {
			return client.checkExists().forPath(path) != null;
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	public byte[] getData(String path) {
		try {
			return client.getData().forPath(path);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	public void setData(String path, byte[] bytes) {
		try {
			client.setData().forPath(path, bytes);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	public List<String> ls(String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	public void persistent(String path) {
		persistent(path, new byte[0]);
	}

	public void persistent(String path, byte[] data) {
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	public void ephemeral(String path) {
		ephemeral(path, new byte[0]);
	}

	public void ephemeral(String path, byte[] data) {
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	public void delete(String path) {
		try {
			client.delete().forPath(path);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, errMsg, e);
		}
	}

	/**
	 * @param path
	 * @param listener
	 */
	public void addPathChildrenListener(String path, PathChildrenCacheListener listener) {
		PathChildrenCache cache = new PathChildrenCache(client, path, true);
		try {
			cache.start();
		} catch (Exception ex) {
			handleException(ex);
		}
		caches.put(path, cache);
		cache.getListenable().addListener(listener);
	}

	/**
	 * @param path
	 * @param listener
	 */
	public void addNodeListener(String path, NodeCacheListener listener) {
		NodeCache cache = new NodeCache(client, path, true);
		try {
			cache.start();
		} catch (Exception ex) {
			handleException(ex);
		}
		caches.put(path, cache);
		cache.getListenable().addListener(listener);
	}

	public void start() {
		client.start();
	}

	public void close() {
		closeCache();
		CloseableUtils.closeQuietly(client);
	}

	public CuratorFramework getClient() {
		return client;
	}

	protected static void handleException(Exception cause) {
		if ((isIgnoredException(cause) || null != cause.getCause()) && isIgnoredException(cause.getCause())) {
			logger.warn("忽略的异常: {}", cause.getMessage());
		} else if (cause instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		} else {
			throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "注册中心异常", cause);
		}
	}

	protected static boolean isIgnoredException(Throwable cause) {
		return null != cause && (cause instanceof KeeperException.ConnectionLossException || cause instanceof KeeperException.NoNodeException
				|| cause instanceof KeeperException.NodeExistsException);
	}

	protected void closeCache() {
		caches.forEach((k, v) -> {
			try {
				v.close();
			} catch (IOException e) {
				handleException(e);
			}
		});
		waitForCacheClose();
	}

	/*
 * 异步处理, 可能会导致client先关闭而cache还未关闭结束.
 * TODO 等待Curator新版本解决这个bug.
 */
	protected void waitForCacheClose() {
		try {
			Thread.sleep(500L);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
