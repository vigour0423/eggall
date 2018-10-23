package com.ddl.egg.job;

/**
 * Created by mark.huang on 2016-07-28.
 */
public class JobRegistry {

    private final JobCollector jobCollector;

    public JobRegistry(JobCollector jobCollector) {
        this.jobCollector = jobCollector;
    }

    public void addJob(JobRegistryEntry jobRegistryEntry) {
        jobCollector.collect(jobRegistryEntry);
    }
}
