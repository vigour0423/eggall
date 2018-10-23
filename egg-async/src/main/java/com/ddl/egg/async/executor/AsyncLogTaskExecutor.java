package com.ddl.egg.async.executor;

import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by mark.huang on 2016-07-05.
 */
public class AsyncLogTaskExecutor extends TaskExecutorAdapter {

    public AsyncLogTaskExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(Executors.newFixedThreadPool(corePoolSize, threadFactory));
    }

}
