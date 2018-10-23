package com.ddl.egg.job.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.LogConstants;
import com.ddl.egg.log.TraceLogger;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Created by mark.huang on 2016-07-30.
 */
public class EnableJobLogListener implements ElasticJobListener {

    private TraceLogger traceLogger = TraceLogger.get();
    private ActionLoggerImpl actionLogger = ActionLoggerImpl.get();

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContext) {
        initActionLog(shardingContext.getJobName());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContext) {
        traceLogger.cleanup(false);
        actionLogger.save();
    }

    private void initActionLog(String action) {
        actionLogger.initialize();
        traceLogger.initialize();

        String requestId = UUID.randomUUID().toString();
        MDC.put(LogConstants.MDC_ACTION, action);
        MDC.put(LogConstants.MDC_REQUEST_ID, requestId);

        ActionLog actionLog = actionLogger.currentActionLog();
        if (actionLog != null) {
            actionLog.setRequestId(requestId);
            actionLog.setAction(action);
        }
    }
}
