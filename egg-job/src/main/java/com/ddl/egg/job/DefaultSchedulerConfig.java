package com.ddl.egg.job;

import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobStatisticsAPI;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.lite.lifecycle.internal.statistics.JobStatisticsAPIImpl;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * .
 * Created by mark.huang on 2016-07-28.
 */
public abstract class DefaultSchedulerConfig {

    static {
        System.setProperty("org.terracotta.quartz.skipUpdateCheck", "true");//关闭quartz自动更新
    }

    @Autowired
    private Environment env;

    @Bean
    public CoordinatorRegistryCenter coordinatorRegistryCenter() {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(env.getProperty("job.serverLists"), env.getProperty("job.namespace"));
        zookeeperConfiguration.setMaxRetries(env.getProperty("job.retry.max", Integer.class, 3));
        zookeeperConfiguration.setMaxSleepTimeMilliseconds(env.getProperty("job.time.sleep.max", Integer.class, 3000));
        zookeeperConfiguration.setBaseSleepTimeMilliseconds(env.getProperty("job.time.sleep.base", Integer.class, 1000));
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }

    @Bean(initMethod = "init")
    public SchedulerManager jobManager() {
        SchedulerManager jobManager = new SchedulerManager(coordinatorRegistryCenter());
        addJobs(new JobRegistry(jobManager));
        return jobManager;
    }

    @Bean
    public JobOperateAPI jobOperateAPI() {
        return new JobOperateAPIImpl(coordinatorRegistryCenter());
    }

    @Bean
    public JobStatisticsAPI jobStatisticsAPI() {
        return new JobStatisticsAPIImpl(coordinatorRegistryCenter());
    }


    protected abstract void addJobs(JobRegistry jobRegistry);
}
