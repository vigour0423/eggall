package com.ddl.egg.common.dubbo.filter;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
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


public class TraceConsumerFilter implements Filter {

    private RuntimeSettings runtimeSettings;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (!TraceContext.getTraceConfig().isEnabled()) {
            return invoker.invoke(invocation);
        }

        Stopwatch watch = Stopwatch.createStarted();
        Span span = startTrace(invoker, invocation);
        TraceContext.start();
        TraceContext.setTraceId(span.getTrace_id());
        TraceContext.setSpanId(span.getId());
        //TraceContext.addSpan(span);
        Result result = invoker.invoke(invocation);
        this.endTrace(span, watch);

        return result;
    }

    private Span startTrace(Invoker<?> invoker, Invocation invocation) {
        Span consumerSpan = new Span();

        long id = IdUtils.get();
        consumerSpan.setId(id);
        Long traceId;
        if (null == TraceContext.getTraceId()) {
            TraceContext.start();
            traceId = id;
        } else {
            traceId = TraceContext.getTraceId();
        }

        consumerSpan.setTrace_id(traceId);
        consumerSpan.setParent_id(TraceContext.getSpanId());
        consumerSpan.setName(toInvokeAction(invoker, invocation));
        long timestamp = System.currentTimeMillis() * 1000;
        consumerSpan.setTimestamp(timestamp);

        consumerSpan.addToAnnotations(
                Annotation.create(timestamp, TraceContext.ANNO_CS,
                        Endpoint.create(
                                TraceContext.getTraceConfig().getApplicationName(),
                                IpUtil.ip2Num(runtimeSettings.getRuntimeIp()),
                                TraceContext.getTraceConfig().getServerPort())));

        Map<String, String> attaches = invocation.getAttachments();
        attaches.put(TraceContext.TRACE_ID_KEY, String.valueOf(consumerSpan.getTrace_id()));
        attaches.put(TraceContext.SPAN_ID_KEY, String.valueOf(consumerSpan.getId()));
        return consumerSpan;
    }

    private String toInvokeAction(Invoker<?> invoker, Invocation invocation) {
        return invoker.getInterface().getName() + TrackSetting.PO + invocation.getMethodName();
    }

    private void endTrace(Span span, Stopwatch watch) {
        span.addToAnnotations(
                Annotation.create(System.currentTimeMillis() * 1000, TraceContext.ANNO_CR,
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
