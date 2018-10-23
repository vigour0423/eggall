package com.ddl.egg.job.concurrent;

import com.ddl.egg.log.LogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.Callable;


public class TaskProxy<T> implements Callable<T> {
    private final Logger logger = LoggerFactory.getLogger(TaskProxy.class);

    private final Callable<T> delegate;
    private final long parentThreadId;
    private final String parentRequestId;
    private final String parentAction;

    public TaskProxy(Callable<T> delegate) {
        this.delegate = delegate;
        parentThreadId = Thread.currentThread().getId();
        parentRequestId = MDC.get(LogConstants.MDC_REQUEST_ID);
        parentAction = MDC.get(LogConstants.MDC_ACTION);
    }

    @Override
    public T call() throws Exception {
        //TODO(neo): central place to handle MDC contexts?
        MDC.clear();    // clear the child thread MDC, since MDC uses InheritableThreadLocal, so values in parent thread won't be cleared
        MDC.put(LogConstants.MDC_TARGET_THREAD_ID, String.valueOf(parentThreadId));
        MDC.put(LogConstants.MDC_REQUEST_ID, parentRequestId);
        MDC.put(LogConstants.MDC_ACTION, parentAction);
        try {
            logger.debug("start task, task={}, currentThread={}", delegate, Thread.currentThread().getId());
            return delegate.call();
        } finally {
            logger.debug("end task, task={}, currentThread={}", delegate, Thread.currentThread().getId());
        }
    }
}
