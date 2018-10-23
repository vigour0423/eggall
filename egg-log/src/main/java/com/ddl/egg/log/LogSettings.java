package com.ddl.egg.log;

public class LogSettings {
    private static final LogSettings INSTANCE = new LogSettings();
    private boolean enableTraceLog = true;
    private boolean alwaysWriteTraceLog;
    private LogMessageFilter logMessageFilter;

    public static LogSettings get() {
        return INSTANCE;
    }

    public boolean isEnableTraceLog() {
        return enableTraceLog;
    }

    public void setEnableTraceLog(boolean enableTraceLog) {
        this.enableTraceLog = enableTraceLog;
    }

    public boolean isAlwaysWriteTraceLog() {
        return alwaysWriteTraceLog;
    }

    public void setAlwaysWriteTraceLog(boolean alwaysWriteTraceLog) {
        this.alwaysWriteTraceLog = alwaysWriteTraceLog;
    }

    public LogMessageFilter getLogMessageFilter() {
        return logMessageFilter;
    }

    public void setLogMessageFilter(LogMessageFilter logMessageFilter) {
        this.logMessageFilter = logMessageFilter;
    }

}
