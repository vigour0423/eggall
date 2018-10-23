package com.ddl.egg.job.concurrent;

import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.LogConstants;
import com.ddl.egg.log.TraceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Created by lincn on 2017/4/13.
 */
public class IndependentRunnableProxy implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(IndependentRunnableProxy.class);

	private final Runnable delegate;

	public IndependentRunnableProxy(Runnable delegate) {
		this.delegate = delegate;
	}

	@Override
	public void run() {
		TraceLogger traceLogger = TraceLogger.get();
		ActionLoggerImpl actionLogger = ActionLoggerImpl.get();
		actionLogger.initialize();
		traceLogger.initialize();
		ActionLog actionLog = actionLogger.currentActionLog();
		String requestId = UUID.randomUUID().toString();
		actionLog.setRequestId(requestId);
		String action = MDC.get(LogConstants.MDC_ACTION);
		MDC.put("MDC_ACTION", action);
		MDC.put("MDC_REQUEST_ID", requestId);
		actionLog.setAction(action);
		try {
			logger.debug("start independent task, task={}", delegate);
			delegate.run();
		} finally {
			logger.debug("end independent task, task={}", delegate);
			traceLogger.cleanup(false);
			actionLogger.save();
		}
	}
}
