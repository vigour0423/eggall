package com.ddl.egg.job.exception;

import com.dangdang.ddframe.job.executor.handler.JobExceptionHandler;
import com.ddl.egg.common.spring.SpringContextHolder;
import com.ddl.egg.log.exception.ErrorHandler;

/**
 * Created by mark on 2017/6/13.
 */
public class CommonJobExceptionHandle implements JobExceptionHandler {


    @Override
    public void handleException(String jobName, Throwable cause) {
        SpringContextHolder.getBean(ErrorHandler.class).handle(cause);
    }
}
