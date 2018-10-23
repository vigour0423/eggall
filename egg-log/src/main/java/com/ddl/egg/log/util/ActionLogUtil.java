package com.ddl.egg.log.util;

import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;

/**
 * Created by mark.huang on 2016-11-25.
 */
public class ActionLogUtil {

    public static void logParam(String key, String value) {
        ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
        if (actionLog != null && value != null) {
            actionLog.logContext(key, value);
        }
    }

}
