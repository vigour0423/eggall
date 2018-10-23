package com.ddl.egg.job.concurrent;

import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.LogConstants;
import com.ddl.egg.log.TraceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by lincn on 2017/4/13.
 */
public class IndependentCallableProxy<T> implements Callable<T> {

	private final Logger logger = LoggerFactory.getLogger(IndependentCallableProxy.class);

	private final Callable<T> delegate;

	public IndependentCallableProxy(Callable<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public T call() throws Exception {
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
			return delegate.call();
		} finally {
			logger.debug("end independent task, task={}", delegate);
			traceLogger.cleanup(false);
			actionLogger.save();
		}
	}
}
