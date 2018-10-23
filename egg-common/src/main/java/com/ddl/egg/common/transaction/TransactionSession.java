package com.ddl.egg.common.transaction;

import com.ddl.egg.common.transaction.extension.ISessionStrategy;
import com.ddl.egg.common.transaction.extension.StrategyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lincn on 2018/1/10.
 */
public class TransactionSession implements ISession {

	private final transient Logger logger = LoggerFactory.getLogger(getClass());

	private String sessionId;

	@Override
	public void send(ISessionStrategy sessionStrategy, StrategyParams strategyParams) {
		SessionSupporter.get().add(new SessionInternal(sessionStrategy, strategyParams));
	}

	public void beginTransaction() {
		logger.debug(sessionId + ":beginTransaction");
	}

	public void commit() {
		logger.debug(sessionId + ":commit,msg size:" + SessionSupporter.get().size());
		SessionSupporter.get().forEach(v->{
			try{
				v.getSessionStrategy().execute(v.getStrategyParams());
			}catch (Exception e){
				logger.error("TransactionSession commit execption:",e);
			}
		});
		SessionSupporter.purge();
	}

	public void rollback() {
		logger.debug(sessionId + ":rollback");
		SessionSupporter.purge();
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
