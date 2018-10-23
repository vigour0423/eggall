package com.ddl.egg.web.platform.web.interceptor;

import com.ddl.egg.log.exception.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * In spring framework to catch exception on view rendering, requires interceptor.afterCompletion
 *
 * @author mark.huang
 */
public class ExceptionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ErrorHandler errorHandler;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        errorHandler.handle(ex);
    }

}
