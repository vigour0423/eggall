package com.ddl.egg.web.platform.web.interceptor;


import com.ddl.egg.log.distribute.IdUtils;
import com.ddl.egg.log.distribute.TraceAgent;
import com.ddl.egg.log.distribute.TraceContext;
import com.ddl.egg.log.util.ClassUtils;
import com.ddl.egg.log.util.IpUtil;
import com.ddl.egg.setting.RuntimeSettings;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


public class TraceInterceptor extends HandlerInterceptorAdapter {

    private static final String DRSTRIBUTE_SPAN_NAME = "DRSTRIBUTE_SPAN_NAME";

    @Autowired
    private RuntimeSettings runtimeSettings;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            Span span = startTrace(String.format("%s-%s", ClassUtils.getSimpleOriginalClassName(((HandlerMethod) handler).getBean()), ((HandlerMethod) handler).getMethod().getName()));
            TraceContext.start();
            TraceContext.setTraceId(span.getTrace_id());
            TraceContext.setSpanId(span.getId());
            //TraceContext.addSpan(span);
            request.setAttribute(DRSTRIBUTE_SPAN_NAME, span);
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            Span span = (Span) request.getAttribute(DRSTRIBUTE_SPAN_NAME);
            if (span != null) {
                span.addToAnnotations(
                        Annotation.create(System.currentTimeMillis() * 1000, TraceContext.ANNO_CR,
                                Endpoint.create(
                                        TraceContext.getTraceConfig().getApplicationName(),
                                        IpUtil.ip2Num(runtimeSettings.getRuntimeIp()),
                                        TraceContext.getTraceConfig().getServerPort())));

                span.setDuration(TraceContext.getWatch().elapsedTime());
                TraceAgent traceAgent = new TraceAgent(TraceContext.getTraceConfig().getZipkinUrl());

                traceAgent.send(Arrays.asList(span));
            }
        }
    }

    private Span startTrace(String action) {
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
        consumerSpan.setName(action);
        long timestamp = System.currentTimeMillis() * 1000;
        consumerSpan.setTimestamp(timestamp);

        consumerSpan.addToAnnotations(
                Annotation.create(timestamp, TraceContext.ANNO_CS,
                        Endpoint.create(
                                TraceContext.getTraceConfig().getApplicationName(),
                                IpUtil.ip2Num(runtimeSettings.getRuntimeIp()),
                                TraceContext.getTraceConfig().getServerPort())));

        return consumerSpan;
    }
}
