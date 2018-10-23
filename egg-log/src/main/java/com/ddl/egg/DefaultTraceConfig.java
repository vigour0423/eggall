package com.ddl.egg;

import com.ddl.egg.log.distribute.TraceConfig;
import com.ddl.egg.log.distribute.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by mark on 2017/5/3.
 */
public class DefaultTraceConfig {

    @Autowired
    public TraceConfig traceConfig;

    @PostConstruct
    public void initTraceContext() {
        TraceContext.init(traceConfig);
    }

}
