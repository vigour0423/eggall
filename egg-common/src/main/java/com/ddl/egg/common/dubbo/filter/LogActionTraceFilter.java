package com.ddl.egg.common.dubbo.filter;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.ddl.egg.common.dubbo.TrackSetting;
import com.ddl.egg.json.JSON;
import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.LogConstants;
import com.ddl.egg.log.TraceLogger;
import com.ddl.egg.log.exception.ErrorHandler;
import com.ddl.egg.log.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;


/**
 * Created by mark.huang on 2016-08-31.
 */
public class LogActionTraceFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(LogActionTraceFilter.class);
    private ErrorHandler errorHandler;
    private TrackSetting trackSetting;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        if (context.isProviderSide()) {
            return monitorProvide(invoker, invocation, context);
        } else {
            return monitorConsumer(invoker, invocation, context);
        }
    }

    private Result monitorProvide(Invoker<?> invoker, Invocation invocation, RpcContext context) {
        TraceLogger traceLogger = TraceLogger.get();
        ActionLoggerImpl actionLogger = ActionLoggerImpl.get();
        String action = StringUtils.EMPTY;
        long startTime = 0;

        try {
            actionLogger.initialize();
            traceLogger.initialize();
            startTime = System.currentTimeMillis();
            logger.debug("====== dubbo service begin ======");
            ActionLog actionLog = actionLogger.currentActionLog();

            String clientIP = context.getRemoteHost();
            String requestId = UUID.randomUUID().toString();
            actionLog.setRequestId(requestId);
            actionLog.setClientIP(clientIP);
            action = toAction(context);
            MDC.put(LogConstants.MDC_ACTION, action);
            MDC.put(LogConstants.MDC_REQUEST_ID, requestId);
            actionLog.setAction(action);

            if (context.getArguments() != null) logArgs(context.getArguments(), actionLog);
            Result result = invoker.invoke(invocation);

            if (result.hasException()) logException(result.getException(), actionLog);
            return result;
        } finally {
            Integer warnTime = trackSetting.get(action);
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (warnTime != null && warnTime > 0 && elapsedTime > warnTime) {
                logger.warn("process took longer than track threshold, elapsedTime={}(ms)", elapsedTime);
            }

            logger.debug("====== dubbo service end ======");
            traceLogger.cleanup(false);
            actionLogger.save();
        }
    }

    private void logException(Throwable throwable, ActionLog actionLog) {
        actionLog.setException(throwable.getClass().getName());
        actionLog.setErrorMessage(throwable.getMessage());
        errorHandler.handle(throwable);
    }

    private void logArgs(Object[] args, ActionLog actionLog) {
        if (trackSetting.isLogRequestParam()) {
            try {
                actionLog.logContext("param", JSON.toJSON(args));
            } catch (Exception e) {
                //有些请求序列化会报错
                actionLog.logContext("param", "error");
            }
        }
    }

    private void debugParams(RpcContext context) {
        try {
            logger.debug("params={}", context.getArguments() == null ? StringUtils.EMPTY : JSON.toJSON(context.getArguments()));
        } catch (Exception e) {
            logger.debug("params={}", "error");
        }
    }

    private Result monitorConsumer(Invoker<?> invoker, Invocation invocation, RpcContext context) {
        debugParams(context);

        StopWatch stopWatch = new StopWatch();
        try {
            return invoker.invoke(invocation);
        } finally {
            logger.debug("invoke dubbo service={}, elapsedTime={}(ms)", toInvokeAction(invoker, invocation), stopWatch.elapsedTime());
        }
    }

    private String toInvokeAction(Invoker<?> invoker, Invocation invocation) {
        return invoker.getInterface().getName() + TrackSetting.PO + invocation.getMethodName();
    }

    private String toAction(RpcContext context) {
        URL url = context.getUrl();
        return url.getParameter("interface") + TrackSetting.PO + context.getMethodName();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setTrackSetting(TrackSetting trackSetting) {
        this.trackSetting = trackSetting;
    }

}

