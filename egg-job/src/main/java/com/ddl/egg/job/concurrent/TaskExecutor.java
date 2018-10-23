package com.ddl.egg.job.concurrent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public final class TaskExecutor {
    private final ExecutorService executorService;

    @Autowired
    private ApplicationContext applicationContext;

    private TaskExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static TaskExecutor fixedSizeExecutor(int threadPoolSize) {
        return new TaskExecutor(Executors.newFixedThreadPool(threadPoolSize));
    }

    public static TaskExecutor unlimitedExecutor() {
        return new TaskExecutor(Executors.newCachedThreadPool());
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }

    // executorService.getActiveCount only returns approximate number, can not be used in exact unit tests
    public long getActiveTaskCount() {
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }

    public <T> Future<T> execute(Callable<T> task) {
        Callable<T> taskBean = initializeBean(task);
        return executorService.submit(new TaskProxy<>(taskBean));
    }

    public <T> List<T> executeAll(List<? extends Callable<T>> tasks) {
        try {
            List<Callable<T>> proxyTasks = createProxyTasks(tasks);
            List<Future<T>> futures = executorService.invokeAll(proxyTasks);
            List<T> results = new ArrayList<>(futures.size());
            for (Future<T> future : futures) {
                results.add(future.get());
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            throw new TaskExecutionException(e);
        }
    }

    private <T> List<Callable<T>> createProxyTasks(List<? extends Callable<T>> tasks) {
        List<Callable<T>> proxyTasks = new ArrayList<>(tasks.size());
        for (Callable<T> task : tasks) {
            Callable<T> taskBean = initializeBean(task);
            proxyTasks.add(new TaskProxy<>(taskBean));
        }
        return proxyTasks;
    }

    private <T> T initializeBean(T bean) {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(bean);
        return (T) beanFactory.initializeBean(bean, bean.getClass().getName());
    }
}
