package com.ddl.egg.async;

import org.aopalliance.aop.Advice;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;

import java.util.concurrent.Executor;

/**
 * Created by mark.huang on 2016-09-11.
 */
public class HKAsyncAnnotationAdvisor extends AsyncAnnotationAdvisor {


    public HKAsyncAnnotationAdvisor(Executor executor, AsyncUncaughtExceptionHandler exceptionHandler) {
        super(executor, exceptionHandler);
    }

    @Override
    protected Advice buildAdvice(Executor executor, AsyncUncaughtExceptionHandler exceptionHandler) {
        return new HKAnnotationAsyncExecutionInterceptor(executor, exceptionHandler);
    }
}
