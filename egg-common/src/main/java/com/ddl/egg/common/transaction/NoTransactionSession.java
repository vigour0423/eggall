package com.ddl.egg.common.transaction;

import com.ddl.egg.common.transaction.extension.ISessionStrategy;
import com.ddl.egg.common.transaction.extension.StrategyParams;

/**
 * Created by lincn on 2018/1/10.
 */
public class NoTransactionSession implements ISession {

	private String sessionId;

	@Override
	public void send(ISessionStrategy sessionStrategy, StrategyParams strategyParams) {
		sessionStrategy.execute(strategyParams);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "NoTransactionSession [sessionId=" + sessionId + "]";
	}
}
