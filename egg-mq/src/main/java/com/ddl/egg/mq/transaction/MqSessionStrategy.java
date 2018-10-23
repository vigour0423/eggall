package com.ddl.egg.mq.transaction;

import com.ddl.egg.common.transaction.extension.ISessionStrategy;
import com.ddl.egg.common.transaction.extension.StrategyParams;
import com.ddl.egg.exception.BusinessException;
import com.ddl.egg.mq.interfaces.IProduceSend;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lincn on 2018/1/10.
 */
public class MqSessionStrategy implements ISessionStrategy {

	private final static   Logger logger = LoggerFactory.getLogger(MqSessionStrategy.class);

	IProduceSend produceSend;

	public MqSessionStrategy(IProduceSend produceSend) {
		this.produceSend = produceSend;
	}

	@Override
	public void execute(StrategyParams params) {
		logger.debug("send");
		if (!getStrategyParamsClass().isAssignableFrom(params.getClass())) {
			logger.error("MqSessionStrategy StrategyParams  must instanceof MqStrategyParams.class,error class:{}",params.getClass());
			throw new BusinessException(HttpStatus.SC_BAD_REQUEST,"MqSessionStrategy 入参类型不合法");
		}
		MqStrategyParams mqStrategyParams = (MqStrategyParams) params;
		produceSend.send(mqStrategyParams.getBody(), mqStrategyParams.getTopic(), mqStrategyParams.getDelayTime(), mqStrategyParams.getTag(), mqStrategyParams.getMsgKey());
	}

	@Override
	public Class<?> getStrategyParamsClass() {
		return MqStrategyParams.class;
	}
}
