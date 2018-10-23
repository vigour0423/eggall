package com.ddl.egg.common.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.base.Stopwatch;
import com.ddl.egg.common.dubbo.TrackSetting;
import com.ddl.egg.log.distribute.IdUtils;
import com.ddl.egg.log.distribute.TraceAgent;
import com.ddl.egg.log.distribute.TraceContext;
import com.ddl.egg.log.util.IpUtil;
import com.ddl.egg.setting.RuntimeSettings;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TraceProviderFilter implements Filter {

    private RuntimeSettings runtimeSettings;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (!TraceContext.getTraceConfig().isEnabled()) {
            return invoker.invoke(invocation);
        }

        Map<String, String> attaches = invocation.getAttachments();
        if (!attaches.containsKey(TraceContext.TRACE_ID_KEY)) {
            return invoker.invoke(invocation);
        }
        Stopwatch watch = Stopwatch.createStarted();
        Span providerSpan = startTrace(attaches);

        Result result = invoker.invoke(invocation);
        endTrace(providerSpan, watch);

        return result;
    }

    private Span startTrace(Map<String, String> attaches) {
        Long traceId = Long.valueOf(attaches.get(TraceContext.TRACE_ID_KEY));
        Long parentSpanId = Long.valueOf(attaches.get(TraceContext.SPAN_ID_KEY));

        TraceContext.start();
        TraceContext.setTraceId(traceId);
        TraceContext.setSpanId(parentSpanId);

        Span providerSpan = new Span();

        long id = IdUtils.get();
        providerSpan.setId(id);
        providerSpan.setParent_id(parentSpanId);
        providerSpan.setTrace_id(traceId);
        providerSpan.setName(toAction());
        long timestamp = System.currentTimeMillis() * 1000;
        providerSpan.setTimestamp(timestamp);

        providerSpan.addToAnnotations(
                Annotation.create(timestamp, TraceContext.ANNO_SR,
                        Endpoint.create(
                                TraceContext.getTraceConfig().getApplicationName(),
                                IpUtil.ip2Num(runtimeSettings.getRuntimeIp()),
                                TraceContext.getTraceConfig().getServerPort())));

        //TraceContext.addSpan(providerSpan);
        return providerSpan;
    }

    private String toAction() {
        RpcContext context = RpcContext.getContext();
        URL url = context.getUrl();
        return url.getParameter("interface") + TrackSetting.PO + context.getMethodName();
    }

    private void endTrace(Span span, Stopwatch watch) {
        span.addToAnnotations(
                Annotation.create(System.currentTimeMillis() * 1000, TraceContext.ANNO_SS,
                        Endpoint.create(
                                TraceContext.getTraceConfig().getApplicationName(),
                                IpUtil.ip2Num(runtimeSettings.getRuntimeIp()),
                                TraceContext.getTraceConfig().getServerPort())));

        span.setDuration(watch.stop().elapsed(TimeUnit.MICROSECONDS));
        TraceAgent traceAgent = new TraceAgent(TraceContext.getTraceConfig().getZipkinUrl());

        traceAgent.send(Arrays.asList(span));
    }

    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }
}
