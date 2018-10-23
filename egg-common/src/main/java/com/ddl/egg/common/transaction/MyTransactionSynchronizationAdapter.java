package com.ddl.egg.common.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * 核心事务同步适配器<br/>
 * 当方法上面定义了@Transactional注解，那么当每次状态发生时就会调用本同步适配器
 * Created by zhuyf
 *
 */
public class MyTransactionSynchronizationAdapter extends TransactionSynchronizationAdapter {

	private final static Logger logger = LoggerFactory.getLogger(MyTransactionSynchronizationAdapter.class);

	private TransactionSessionHolder sessionHolder;

	public MyTransactionSynchronizationAdapter(TransactionSessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	@Override
	public void beforeCommit(boolean readOnly) {
		logger.debug("beforeCommit");
		//readOnly标识是否是一个只读线程
		if (!readOnly) {
			TransactionSession transactionSession = (TransactionSession) TransactionSynchronizationManager.getResource(sessionHolder);
			transactionSession.beginTransaction();
		}
	}

	@Override
	public void afterCompletion(int status) {
		logger.debug("afterCompletion");
		TransactionSession transactionSession = (TransactionSession) TransactionSynchronizationManager.getResource(sessionHolder);
		if (STATUS_COMMITTED == status) {
			transactionSession.commit();
		} else if (STATUS_ROLLED_BACK == status) {
			transactionSession.rollback();
		} else {
			throw new RuntimeException("未知的事务状态");
		}
		TransactionSynchronizationManager.unbindResourceIfPossible(sessionHolder);
		CustomSessionFactory.close();
	}
}
