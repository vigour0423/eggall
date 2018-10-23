package com.ddl.egg.mq.enhancer;

import com.aliyun.openservices.ons.api.Message;
import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.TraceLogger;
import com.ddl.egg.log.exception.ErrorHandler;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by admin on 2016/12/14.
 */
public abstract class LogEnhancerMqConsumerHandle extends MongoEnhancerMqConsumerHandle {
	TraceLogger traceLogger = TraceLogger.get();
	ActionLoggerImpl actionLogger = ActionLoggerImpl.get();

	@Autowired
	protected ErrorHandler errorHandler;

	@Override
	protected void logBegin(Message msg) {
		traceLogger.initialize();
		actionLogger.initialize();
		ActionLog actionLog = actionLogger.currentActionLog();
		String requestId = UUID.randomUUID().toString();
		actionLog.setRequestId(requestId);
		String action = getActionName(msg);
		MDC.put("MDC_ACTION", action);
		MDC.put("MDC_REQUEST_ID", requestId);
		actionLog.setAction(action);
	}

	@Override
	protected void logEnd() {
		traceLogger.cleanup(false);
		actionLogger.save();
	}

	@Override
	protected void handleMqException(Exception e) {
		errorHandler.handle(e);
	}

	protected String getActionName(Message msg) {
		String action = LogEnhancerMqConsumerHandle.class.getName();
		try {
			action = action + msg.getTopic();
		} catch (Exception e) {

		}
		return action;
	}

}
