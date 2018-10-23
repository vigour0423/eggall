package com.ddl.egg.web.platform.web.request;

import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.LogConstants;
import com.ddl.egg.log.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;


public class RequestContextInterceptor extends HandlerInterceptorAdapter {
    public static final String HEADER_REQUEST_ID = "request-id";
    private static final String PARAM_REQUEST_ID = "_requestId";
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = RequestContextInterceptor.class.getName() + ".CONTEXT_INITIALIZED";

    private final Logger logger = LoggerFactory.getLogger(RequestContextInterceptor.class);
    private RequestContextImpl requestContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // make sure only init once per request
        if (!initialized(request)) {
            logger.debug("initialize requestContext");

            requestContext.setHTTPRequest(request);

            assignRequestId(request);
            assignRequestDate();

            logRequest();

            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }

        assignAction(handler);

        return true;
    }

    public boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    private void logRequest() {
        logger.debug("clientRequestedFullURL={}", requestContext.getClientRequestedFullURLWithQueryString());
        logger.debug("clientRequestedRelativeURL={}", requestContext.getClientRequestedRelativeURL());
        logger.debug("remoteAddressClientIP={}", requestContext.getRemoteAddress().getClientIP());

        ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
        actionLog.setRequestURI(requestContext.getClientRequestedRelativeURLWithQueryString());
        actionLog.setRequestId(requestContext.getRequestId());
        actionLog.setClientIP(requestContext.getRemoteAddress().getClientIP());
    }

    private void assignRequestDate() {
        Date now = new Date();
        requestContext.setRequestDate(now);
        logger.debug("requestDate={}", now);
        logger.debug("clientIp={}", requestContext.getRemoteAddress().getClientIP());
    }

    // use the first handleMethod in call stack, due to http forwarding, the stack can be view->handlerMethod->handlerMethod
    void assignAction(Object handler) {
        String handlerDescription = handlerDescription(handler);
        logger.debug("currentHandler={}", handlerDescription);

        if (requestContext.getAction() == null && handler instanceof HandlerMethod) {
            requestContext.setAction(handlerDescription);
            ActionLoggerImpl.get().setCurrentAction(handlerDescription);
            MDC.put(LogConstants.MDC_ACTION, handlerDescription);
            logger.debug("requestAction={}", handlerDescription);
        }
    }

    private String handlerDescription(Object handler) {
        if (handler instanceof HandlerMethod) {
            return String.format("%s-%s", ClassUtils.getSimpleOriginalClassName(((HandlerMethod) handler).getBean()), ((HandlerMethod) handler).getMethod().getName());
        } else if (handler instanceof ParameterizableViewController) {
            return ((ParameterizableViewController) handler).getViewName();
        }
        throw new IllegalStateException("unknown handler, handler=" + handler);
    }

    private void assignRequestId(HttpServletRequest request) {
        String requestId = getRequestId(request);
        RequestIdValidator.validateRequestId(requestId);
        requestContext.setRequestId(requestId);
        MDC.put(LogConstants.MDC_REQUEST_ID, requestId);
        logger.debug("requestId={}", requestId);
    }

    private String getRequestId(HttpServletRequest request) {
        String requestIdFromHeader = request.getHeader(HEADER_REQUEST_ID);
        if (StringUtils.hasText(requestIdFromHeader)) return requestIdFromHeader;
        String requestIdFromParam = request.getParameter(PARAM_REQUEST_ID);
        if (StringUtils.hasText(requestIdFromParam)) return requestIdFromParam;
        logger.debug("request headers do not contain request-id, generate new one");
        return UUID.randomUUID().toString();
    }

    @Autowired
    public void setRequestContext(RequestContextImpl requestContext) {
        this.requestContext = requestContext;
    }

}
