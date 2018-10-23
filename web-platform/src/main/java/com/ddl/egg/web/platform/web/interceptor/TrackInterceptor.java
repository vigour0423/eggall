package com.ddl.egg.web.platform.web.interceptor;


import com.ddl.egg.log.monitor.Track;
import com.ddl.egg.web.platform.web.ControllerHelper;
import com.ddl.egg.web.platform.web.request.RequestContext;
import com.ddl.egg.web.platform.web.request.RequestContextInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class TrackInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(TrackInterceptor.class);
    private RequestContext requestContext;
    private RequestContextInterceptor requestContextInterceptor;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Assert.isTrue(requestContextInterceptor.initialized(request), "trackInterceptor depends on requestContextInterceptor, please check WebConfig");

        Track track = ControllerHelper.findMethodOrClassLevelAnnotation(handler, Track.class);
        if (track != null) {
            trackProcess(track);
        }
    }

    private void trackProcess(Track track) {
        Date startTime = requestContext.getRequestDate();

        long elapsedTime = System.currentTimeMillis() - startTime.getTime();
        if (warningEnabled(track) && elapsedTime > track.warningThresholdInMs()) {
            logger.warn("process took longer than track threshold, elapsedTime={}(ms)", elapsedTime);
        }
    }

    private boolean warningEnabled(Track track) {
        return track.warningThresholdInMs() > 0;
    }

    @Autowired
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Autowired
    public void setRequestContextInterceptor(RequestContextInterceptor requestContextInterceptor) {
        this.requestContextInterceptor = requestContextInterceptor;
    }
}
