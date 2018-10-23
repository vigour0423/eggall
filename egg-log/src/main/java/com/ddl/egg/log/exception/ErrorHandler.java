package com.ddl.egg.log.exception;

import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionResult;
import com.ddl.egg.log.ActionLoggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);


    private ActionLoggerImpl actionLogger = ActionLoggerImpl.get();

    public void handle(Throwable e) {
        if (isIgnore(e))
            return;
        if (isWarning(e)) {
            logWarning(e);
        } else {
            logError(e);
        }
    }

    boolean isIgnore(Throwable e) {
        if (null == e)
            return true;
        return e.getClass().isAnnotationPresent(Ignore.class);
    }

    boolean isWarning(Throwable e) {
        return e.getClass().isAnnotationPresent(Warning.class) || isWarningException(e);
    }

    private boolean isWarningException(Throwable e) {
        return e instanceof IllegalArgumentException;
    }

    private void logError(Throwable e) {
        ActionLog actionLog = actionLogger.currentActionLog();
        actionLog.setResult(ActionResult.ERROR);
        logger.error(e.getMessage(), e);
        actionLog.setException(e.getClass().getName());
        actionLog.setErrorMessage(e.getMessage());
    }

    private void logWarning(Throwable e) {
        ActionLog actionLog = actionLogger.currentActionLog();
        actionLog.setResult(ActionResult.WARNING);
        logger.info(e.getMessage(), e);
        actionLog.setException(e.getClass().getName());
        actionLog.setErrorMessage(e.getMessage());
    }

}
