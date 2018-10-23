package com.ddl.egg.common.transaction.extension;

/**
 * Created by lincn on 2018/1/10.
 */
public interface ISessionStrategy {

	void execute(StrategyParams params);

	Class<?> getStrategyParamsClass();

}
