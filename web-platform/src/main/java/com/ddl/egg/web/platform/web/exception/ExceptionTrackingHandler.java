package com.ddl.egg.web.platform.web.exception;

import com.ddl.egg.log.exception.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExceptionTrackingHandler extends AbstractHandlerExceptionResolver {

    @Autowired
    private ErrorHandler errorHandler;

    public ExceptionTrackingHandler() {
        setOrder(HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        errorHandler.handle(ex);
        return null;
    }

}
