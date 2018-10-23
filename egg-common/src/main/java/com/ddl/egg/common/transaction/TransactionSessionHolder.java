package com.ddl.egg.common.transaction;


import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

/**
 * Created by lincn on 2018/1/10.
 */
public class TransactionSessionHolder {

	/**
	 * 如果当前线程存在Session，就使用该Session，否者开启一个新的Session
	 *
	 * @return
	 */
	public TransactionSession getSession() {
		if (TransactionSynchronizationManager.hasResource(this)) {
			return getCurrentSession();
		} else {
			return openSession();
		}
	}

	/**
	 * 开启一个新MySession
	 *
	 * @return
	 */
	private TransactionSession openSession() {
		TransactionSession transactionSession = new TransactionSession();
		transactionSession.setSessionId(UUID.randomUUID().toString());
		//注册进当前线程管理一个Synchronization
		TransactionSynchronization transactionSynchronization = new MyTransactionSynchronizationAdapter(this);
		TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);

		//绑定新开启的一个MySession进当前线程事务管理器
		TransactionSynchronizationManager.bindResource(this, transactionSession);
		return transactionSession;
	}

	/**
	 *
	 * @return
	 */
	private TransactionSession getCurrentSession() {
		TransactionSession transactionSession = (TransactionSession) TransactionSynchronizationManager.getResource(this);
		return transactionSession;
	}


}