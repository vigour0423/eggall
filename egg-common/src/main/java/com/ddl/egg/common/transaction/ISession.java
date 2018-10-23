package com.ddl.egg.common.transaction;

import com.ddl.egg.common.transaction.extension.ISessionStrategy;
import com.ddl.egg.common.transaction.extension.StrategyParams;

/**
 * Created by lincn on 2018/1/10.
 */
public interface ISession {

	void send(ISessionStrategy sessionStrategy,StrategyParams strategyParams);

	String getSessionId();

	void setSessionId(String sessionId);
}
