package com.ddl.egg.web.platform.web.filter;


import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.TraceLogger;
import com.ddl.egg.log.util.CharacterEncodings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


public class PlatformFilter implements Filter {
    private static final String PARAM_FORCE_FLUSH_TRACE_LOG = "_trace";
    public static final String REFERER = "referer";
    private final Logger logger = LoggerFactory.getLogger(PlatformFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            filter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void filter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(CharacterEncodings.UTF_8);
        TraceLogger traceLogger = TraceLogger.get();
        ActionLoggerImpl actionLogger = ActionLoggerImpl.get();
        try {
            traceLogger.initialize();
            actionLogger.initialize();
            logger.debug("=== begin request processing ===");
            RequestWrapper requestWrapper = new RequestWrapper(request);
            logRequest(requestWrapper, request);
            chain.doFilter(requestWrapper, response);
        } finally {
            logResponse(response, request);
            logger.debug("=== finish request processing ===");
            traceLogger.cleanup(flushTraceLog(request));
            actionLogger.save();
        }
    }

    private void logResponse(HttpServletResponse response, HttpServletRequest request) {
        int status = response.getStatus();
        logger.debug("responseHTTPStatus={}", status);
        logHeaders(response);
        ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
        actionLog.setHTTPStatusCode(status);
        if (!StringUtils.hasText(actionLog.getRequestURI())) {
            actionLog.setRequestURI(request.getRequestURI());
        }
    }

    private void logHeaders(HttpServletResponse response) {
        for (String name : response.getHeaderNames()) {
            logger.debug("[response-header] {}={}", name, response.getHeader(name));
        }
    }

    private void logRequest(RequestWrapper requestWrapper, HttpServletRequest originalRequest) throws IOException {
        logger.debug("originalRequestURL={}", originalRequest.getRequestURL());
        logger.debug("originalServerPort={}", originalRequest.getServerPort());
        logger.debug("originalContextPath={}", originalRequest.getContextPath());
        logger.debug("originalMethod={}", originalRequest.getMethod());
        logger.debug("dispatcherType={}", originalRequest.getDispatcherType());
        logger.debug("serverPort={}", requestWrapper.getServerPort());
        logger.debug("localPort={}", requestWrapper.getLocalPort());
        ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
        logHeaders(originalRequest, actionLog);
        logParameters(originalRequest);
        logger.debug("remoteAddress={}", originalRequest.getRemoteAddr());

        if (requestWrapper.isPreLoadBody()) {
            logger.debug("body={}", requestWrapper.getOriginalBody());
        }


        actionLog.setHTTPMethod(originalRequest.getMethod());
    }

    private void logHeaders(HttpServletRequest request, ActionLog actionLog) {
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = (String) headers.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.debug("[header] {}={}", headerName, headerValue);
            if (REFERER.equalsIgnoreCase(headerName)) {
                actionLog.logContext(REFERER, headerValue);
            }
        }
    }

    private void logParameters(HttpServletRequest request) {
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            logger.debug("[param] {}={}", paramName, request.getParameter(paramName));
        }
    }

    private boolean flushTraceLog(ServletRequest request) {
        String traceParam = request.getParameter(PARAM_FORCE_FLUSH_TRACE_LOG);
        return "true".equals(traceParam);
    }

    public void destroy() {
    }
}
