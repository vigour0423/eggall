package com.ddl.egg.web.platform.web.interceptor;

import com.ddl.egg.log.ActionLog;
import com.ddl.egg.log.ActionLoggerImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Mark
 */
public class LogHeaderInterceptor extends HandlerInterceptorAdapter {

    private static final String BACKEND_OPERATOR = "backend_operator";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
            String backendOperatorInfo = request.getHeader(BACKEND_OPERATOR);
            if (actionLog != null && StringUtils.hasText(backendOperatorInfo)) {
                actionLog.logContext(BACKEND_OPERATOR, backendOperatorInfo);
            }
        }
        return true;
    }

}
