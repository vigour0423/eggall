package com.ddl.egg.async;

import com.ddl.egg.async.executor.AsyncLogTaskExecutor;
import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.LogConstants;
import com.ddl.egg.log.TraceLogger;
import com.ddl.egg.log.monitor.Track;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * Created by mark.huang on 2016-09-11.
 */
public class HKAnnotationAsyncExecutionInterceptor extends AnnotationAsyncExecutionInterceptor {

    private final Logger log = LoggerFactory.getLogger(HKAnnotationAsyncExecutionInterceptor.class);
    private static final String ACTION_PREFIX = "AsyncTask";

    public HKAnnotationAsyncExecutionInterceptor(Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandle) {
        super(defaultExecutor, exceptionHandle);
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);
        if (executor == null) {
            throw new IllegalStateException(
                    "No executor specified and no default executor set on AsyncExecutionInterceptor either");
        }
        boolean logAbleExecutor = isLogAbleExecutor(executor);
        ActionLog originalActionLog = ActionLoggerImpl.get() == null ? null : ActionLoggerImpl.get().currentActionLog();

        Callable<Object> task = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                MDC.clear();
                TraceLogger traceLogger = null;
                ActionLoggerImpl actionLogger = null;
                long startTime = System.currentTimeMillis();
                try {
                    if (logAbleExecutor) {
                        traceLogger = TraceLogger.get();
                        actionLogger = ActionLoggerImpl.get();
                        initLog(traceLogger, actionLogger, originalActionLog, getActionName(userDeclaredMethod));
                    }

                    //执行业务逻辑
                    log.debug("====== async task begin ======");
                    Object result = invocation.proceed();
                    if (result instanceof Future) {
                        return ((Future<?>) result).get();
                    }
                } catch (ExecutionException ex) { //ReturnType Future not test yet
                    handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
                } catch (Throwable ex) {
                    handleError(ex, userDeclaredMethod, invocation.getArguments());
                } finally {
                    Track track = userDeclaredMethod.getAnnotation(Track.class);//todo handle other position?
                    if (track != null && track.warningThresholdInMs() > 0) {
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        if (elapsedTime > track.warningThresholdInMs()) {
                            log.warn("process took longer than track threshold, elapsedTime={}(ms)", elapsedTime);
                        }
                    }
                    log.debug("====== async task end ======");
                    if (logAbleExecutor) {
                        if (traceLogger != null) traceLogger.cleanup(false);
                        if (actionLogger != null) actionLogger.save();
                    }
                }
                return null;
            }
        };

        return doSubmit(task, executor, invocation.getMethod().getReturnType());
    }

    private boolean isLogAbleExecutor(AsyncTaskExecutor executor) {
        return executor instanceof AsyncLogTaskExecutor;
    }

    private String getActionName(Method userDeclaredMethod) {
        return userDeclaredMethod.getDeclaringClass().getSimpleName() + "-" + userDeclaredMethod.getName() + "-" + ACTION_PREFIX;
    }

    private void initLog(TraceLogger traceLogger, ActionLoggerImpl actionLogger, ActionLog sourceAction, String actionName) {
        traceLogger.initialize();
        actionLogger.initialize();

        String requestId = sourceAction == null ? UUID.randomUUID().toString() : sourceAction.getRequestId();
        String clientIp = sourceAction == null ? "" : sourceAction.getClientIP();
        String requestURI = sourceAction == null ? "" : sourceAction.getRequestURI();
        MDC.put(LogConstants.MDC_REQUEST_ID, requestId);
        MDC.put(LogConstants.MDC_ACTION, actionName);

        actionLogger.currentActionLog().setClientIP(clientIp);
        actionLogger.currentActionLog().setRequestURI(requestURI);
        actionLogger.currentActionLog().setRequestId(requestId);
        actionLogger.currentActionLog().setAction(actionName);
    }
}
