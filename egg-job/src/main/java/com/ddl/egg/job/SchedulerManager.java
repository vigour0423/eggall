package com.ddl.egg.job;

import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.ddl.egg.job.listener.EnableJobLogListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mark.huang on 2016-07-28.
 */
public class SchedulerManager implements JobCollector {

    private final List<JobRegistryEntry> jobRegistryEntries = new ArrayList<>();

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private ElasticJobListener listener = new EnableJobLogListener();
    private boolean isInit = false;

    public SchedulerManager(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        this.coordinatorRegistryCenter = coordinatorRegistryCenter;
    }

    public void init() {
        if (!isInit) {
            coordinatorRegistryCenter.init();
            initJob();
            isInit = true;
        }
    }

    @SuppressWarnings("unchecked")
    private void initJob() {
        jobRegistryEntries.forEach(jobRegistryEntry -> {
            doInit(jobRegistryEntry);
        });
    }

    public void initJobRuntime(JobRegistryEntry jobRegistryEntry) {
        doInit(jobRegistryEntry);
    }

    private void doInit(JobRegistryEntry jobRegistryEntry) {
        ArrayList<ElasticJobListener> listeners = new ArrayList(Arrays.asList(jobRegistryEntry.getElasticJobListeners()));
        if (jobRegistryEntry.isEnableLog())
            listeners.add(0, listener);
        SpringJobScheduler jobScheduler = new SpringJobScheduler(jobRegistryEntry.getElasticJob(), coordinatorRegistryCenter, jobRegistryEntry.getLiteJobConfiguration(), listeners.toArray(new ElasticJobListener[0]));
        jobScheduler.init();
    }

    @Override
    public void collect(JobRegistryEntry jobRegistryEntry) {
        jobRegistryEntries.add(jobRegistryEntry);
    }

}
