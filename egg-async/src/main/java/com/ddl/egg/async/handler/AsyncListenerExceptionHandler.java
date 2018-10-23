package com.ddl.egg.async.handler;

import com.ddl.egg.log.exception.ErrorHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Created by mark.huang on 2016-07-05.
 */
public class AsyncListenerExceptionHandler implements AsyncUncaughtExceptionHandler {

    private ErrorHandler errorHandler;

    public AsyncListenerExceptionHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        this.errorHandler.handle(ex); //todo new handle for ex,method?
    }
}
