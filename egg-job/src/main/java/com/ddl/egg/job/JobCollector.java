package com.ddl.egg.job;

/**
 * Created by mark.huang on 2016-07-28.
 */
public interface JobCollector {

    void collect(JobRegistryEntry jobRegistryContext);

}
