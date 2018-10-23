package com.ddl.egg.common.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Created by lincn on 2018/1/10.
 */
public class CustomSessionFactory {

	private final static Logger logger = LoggerFactory.getLogger(CustomSessionFactory.class);

	private static final ThreadLocal threadLocal = new ThreadLocal();

	public static ISession getSession() {
		//没有开始事务情况下,直接new一个session
		if (!TransactionSynchronizationManager.isActualTransactionActive()) {
			logger.debug("选择 NoTransactionSession");
			return new NoTransactionSession();
		}
		logger.debug("选择 TransactionSession");
		Object var = threadLocal.get();
		if (var == null) {
			var = new TransactionSessionHolder();
			threadLocal.set(var);
		}
		return ((TransactionSessionHolder) var).getSession();
	}

	public static void close() {
		threadLocal.remove();
	}

}
