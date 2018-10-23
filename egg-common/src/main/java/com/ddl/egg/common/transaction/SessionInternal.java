package com.ddl.egg.common.transaction;

import com.ddl.egg.common.transaction.extension.ISessionStrategy;
import com.ddl.egg.common.transaction.extension.StrategyParams;

/**
 * Created by lincn on 2018/1/10.
 */
public class SessionInternal {

	public SessionInternal(ISessionStrategy sessionStrategy, StrategyParams strategyParams) {
		this.sessionStrategy = sessionStrategy;
		this.strategyParams = strategyParams;
	}

	private ISessionStrategy sessionStrategy;

	private StrategyParams strategyParams;

	public ISessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(ISessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

	public StrategyParams getStrategyParams() {
		return strategyParams;
	}

	public void setStrategyParams(StrategyParams strategyParams) {
		this.strategyParams = strategyParams;
	}
}
