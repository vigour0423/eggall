package com.ddl.egg.job;


import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import org.springframework.util.Assert;

public class JobRegistryEntry {

    private LiteJobConfiguration liteJobConfiguration;
    private ElasticJobListener[] elasticJobListeners;
    private boolean enableLog;
    private ElasticJob elasticJob;

    public JobRegistryEntry(ElasticJob elasticJob, LiteJobConfiguration liteJobConfiguration) {
        this(elasticJob, liteJobConfiguration, true);
    }

    public JobRegistryEntry(ElasticJob elasticJob, LiteJobConfiguration liteJobConfiguration, boolean enableLog) {
        this(elasticJob, liteJobConfiguration, null, enableLog);
    }

    public JobRegistryEntry(ElasticJob elasticJob, LiteJobConfiguration liteJobConfiguration, ElasticJobListener[] elasticJobListeners, boolean enableLog) {
        Assert.notNull(elasticJob);
        Assert.notNull(liteJobConfiguration);
        this.elasticJob = elasticJob;
        this.liteJobConfiguration = liteJobConfiguration;
        this.enableLog = enableLog;
        this.elasticJobListeners = elasticJobListeners == null ? new ElasticJobListener[0] : elasticJobListeners;
    }

    public LiteJobConfiguration getLiteJobConfiguration() {
        return liteJobConfiguration;
    }

    public ElasticJobListener[] getElasticJobListeners() {
        return elasticJobListeners;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public ElasticJob getElasticJob() {
        return elasticJob;
    }
}
