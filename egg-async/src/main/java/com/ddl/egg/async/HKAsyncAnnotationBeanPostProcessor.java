package com.ddl.egg.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;

import java.util.concurrent.Executor;

/**
 * Created by mark.huang on 2016-09-11.
 */
public class HKAsyncAnnotationBeanPostProcessor extends AsyncAnnotationBeanPostProcessor {

    private Executor executor;

    private AsyncUncaughtExceptionHandler exceptionHandler;

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void setExceptionHandler(AsyncUncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);

        HKAsyncAnnotationAdvisor advisor = new HKAsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
        advisor.setBeanFactory(beanFactory);
        this.advisor = advisor;
    }
}
