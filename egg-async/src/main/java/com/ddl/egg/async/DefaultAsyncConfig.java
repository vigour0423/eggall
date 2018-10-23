package com.ddl.egg.async;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ddl.egg.log.exception.ErrorHandler;
import com.ddl.egg.async.executor.AsyncLogTaskExecutor;
import com.ddl.egg.async.handler.AsyncListenerExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by mark.huang on 2016-07-05.
 */
public abstract class DefaultAsyncConfig {

    private static final int THREAD_SIZE = 500;

    @Autowired
    private ErrorHandler errorHandler;

    /**
     * FixedThreadPool 线程数默认500
     */
    @Bean(name = AsyncConstants.LOG_TASK_EXECUTOR)
    public AsyncLogTaskExecutor logTaskExecutor() {
        return new AsyncLogTaskExecutor(logTaskExecutorThreadSize(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefixExecutorName() + "-log-async-exec-%d").build());
    }

    protected int logTaskExecutorThreadSize() {
        return THREAD_SIZE;
    }

    @Bean
    public HKAsyncAnnotationBeanPostProcessor asyncAdvisor() {
        HKAsyncAnnotationBeanPostProcessor bpp = new HKAsyncAnnotationBeanPostProcessor();

        bpp.setExecutor(getAsyncExecutor());
        bpp.setExceptionHandler(getAsyncUncaughtExceptionHandler());
        bpp.setProxyTargetClass(isProxyTargetClass());
        return bpp;
    }

    public Executor getAsyncExecutor() {
        return Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefixExecutorName() + "-async-exec-%d").build());
    }

    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncListenerExceptionHandler(errorHandler);
    }

    public boolean isProxyTargetClass() {
        return false;
    }

    protected abstract String prefixExecutorName();
}
