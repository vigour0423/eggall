package com.ddl.egg.web.platform.web.interceptor;

import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.web.platform.web.filter.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * @author Mark
 */
public class RequestBodyInterceptor extends HandlerInterceptorAdapter {

    private static final String LOG_PARAM = "body";

    @Autowired
    private ActionLoggerImpl actionLogger;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (handler instanceof HandlerMethod && request instanceof RequestWrapper) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LogBody logRequest = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), LogBody.class);

            RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequestMapping.class);
            RequestWrapper requestWrapper = (RequestWrapper) request;

            if (logRequest != null && needLogMapping(requestMapping)) {
                doLogRequest(requestWrapper);
            }
        }

        return true;
    }

    private boolean needLogMapping(RequestMapping requestMapping) {
        if (requestMapping == null) return false;

        List<RequestMethod> methods = Arrays.asList(requestMapping.method());
        return methods.contains(RequestMethod.POST) || methods.contains(RequestMethod.PUT);
    }

    private void doLogRequest(RequestWrapper requestWrapper) {
        String body = requestWrapper.getBody();
        if (actionLogger != null && StringUtils.hasText(body)) {
            actionLogger.logContext(LOG_PARAM, body.replaceAll("\n", ""));
        }
    }

}
