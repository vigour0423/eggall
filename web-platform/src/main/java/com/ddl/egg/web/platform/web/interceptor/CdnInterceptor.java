package com.ddl.egg.web.platform.web.interceptor;

import com.ddl.egg.web.platform.constant.SiteConstants;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mark.huang on 2016-09-08.
 */
public class CdnInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod && response != null) {
            response.setHeader(SiteConstants.CACHE_CONTROL, SiteConstants.NO_CACHE);
            response.setDateHeader(SiteConstants.EXPIRES, SiteConstants.NO_EXPIRES);
            response.setHeader(SiteConstants.PRAGMA, SiteConstants.NO_CACHE);
        }
        return true;
    }
}