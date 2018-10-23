package com.ddl.egg.log;

import com.ddl.egg.log.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ActionLoggerImpl {
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String LOG_SPLITTER = " | ";
    private static final int LOG_EXCEPTION_LENGTH = 50;

    // the reason not using Spring is because I think we need to handle scheduler/task executor/MQListener and so on
    private static final ActionLoggerImpl INSTANCE = new ActionLoggerImpl();

    public static ActionLoggerImpl get() {
        return INSTANCE;
    }

    private final Logger logger = LoggerFactory.getLogger(ActionLoggerImpl.class);
    private final ConcurrentMap<Long, ActionLog> logs = new ConcurrentHashMap<>();

    public void initialize() {
        long threadId = Thread.currentThread().getId();
        logs.put(threadId, new ActionLog());
    }

    public void logContext(String key, String value) {
        currentActionLog().logContext(key, value);
    }

    private long getTargetThreadId() {
        String targetThreadId = MDC.get(LogConstants.MDC_TARGET_THREAD_ID);
        return ConvertUtil.toLong(targetThreadId, Thread.currentThread().getId());
    }

    public ActionLog currentActionLog() {
        return logs.get(getTargetThreadId());
    }

    public void setCurrentAction(String action) {
        currentActionLog().setAction(action);
    }

    public void save() {
        long threadId = Thread.currentThread().getId();
        ActionLog actionLog = logs.remove(threadId);
        actionLog.setElapsedTime(System.currentTimeMillis() - actionLog.getRequestDate().getTime());
        logger.info(buildActionLogText(actionLog));
    }

    private String buildActionLogText(ActionLog actionLog) {
        StringBuilder builder = new StringBuilder();
        builder.append(ConvertUtil.toString(actionLog.getRequestDate(), LOG_DATE_FORMAT))
                .append(LOG_SPLITTER)
                .append(actionLog.getResult())
                .append(LOG_SPLITTER)
                .append(actionLog.getRequestId())
                .append(LOG_SPLITTER)
                .append(actionLog.getAction())
                .append(LOG_SPLITTER)
                .append(actionLog.getElapsedTime())
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getClientIP()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getRequestURI()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getHTTPMethod()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getHTTPStatusCode()))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getException()))
                .append(LOG_SPLITTER)
                .append(removeLineBreak(buildLogField(actionLog.getErrorMessage())))
                .append(LOG_SPLITTER)
                .append(buildLogField(actionLog.getTraceLogPath()));

        Map<String, String> context = actionLog.getContext();
        if (context != null) {
            for (Map.Entry<String, String> entry : context.entrySet()) {
                builder.append(LOG_SPLITTER)
                        .append(entry.getKey()).append('=').append(entry.getValue());
            }
        }

        return builder.toString();
    }

    private String buildLogField(Object field) {
        return field == null ? "" : String.valueOf(field);
    }

    private String removeLineBreak(String value) {
        if (StringUtils.hasText(value)) {
            String result = value;
            if (value.length() > LOG_EXCEPTION_LENGTH) {
                result = value.substring(0, LOG_EXCEPTION_LENGTH);
            }
            return result.replaceAll("[\r|\n]", "");
        }
        return value;
    }
}
