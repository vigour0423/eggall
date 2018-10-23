package com.ddl.egg.rest.client.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;

public class APIUriBuilder {

    private String serverURL;
    private String action;
    private String encoding;
    private PathVariableProcessor pathVariableProcessor;
    private RequestParamProcessor requestParamProcessor;

    public String build() {
        StringBuilder uriBuilder = new StringBuilder();

        if (serverURL.endsWith("/"))
            uriBuilder.append(StringUtils.removeEnd(serverURL, "/"));
        else
            uriBuilder.append(serverURL);

        if (pathVariableProcessor != null)
            action = pathVariableProcessor.url(action);
        if (requestParamProcessor != null)
            action = requestParamProcessor.url(action, encoding);

        if (action.startsWith("/"))
            uriBuilder.append(action);
        else
            uriBuilder.append('/').append(action);

        return uriBuilder.toString();
    }

    public APIUriBuilder withServerURL(String serverURL) {
        this.serverURL = serverURL;
        return this;
    }

    public APIUriBuilder withAction(String action) {
        this.action = action;
        return this;
    }

    public APIUriBuilder withEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public APIUriBuilder withAnnotations(Annotation[][] argsAnnotations, Object[] args) {
        for (int i = 0; i < argsAnnotations.length; i++) {
            Annotation[] paramAnnotations = argsAnnotations[i];
            for (Annotation annotation : paramAnnotations) {
                Object obj = args[i];
                if (PathVariable.class.equals(annotation.annotationType())) {
                    PathVariable pathVariable = (PathVariable) annotation;
                    getPathVariableProcessor().getPathVariableMap().put(pathVariable.value(), obj);
                    continue;
                }
                if (RequestParam.class.equals(annotation.annotationType())) {
                    RequestParam requestParam = (RequestParam) annotation;
                    getRequestParamProcessor().getRequestParamMap().put(requestParam.value(), obj);
                    continue;
                }
            }
        }
        return this;
    }

    private PathVariableProcessor getPathVariableProcessor() {
        if (null == pathVariableProcessor)
            pathVariableProcessor = new PathVariableProcessor();
        return pathVariableProcessor;
    }

    private RequestParamProcessor getRequestParamProcessor() {
        if (null == requestParamProcessor)
            requestParamProcessor = new RequestParamProcessor();
        return requestParamProcessor;
    }
}
