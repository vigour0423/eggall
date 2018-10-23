package com.ddl.egg.web.platform.web.request;


import com.ddl.egg.web.platform.request.RemoteAddress;

import java.util.Date;


public interface RequestContext {
    String getClientRequestedFullURLWithQueryString();

    /**
     * get client/browser requested relative url, it doesn't include servletContext or deploymentContext.
     *
     * @return logical relative url
     */
    String getClientRequestedRelativeURLWithQueryString();

    String getClientRequestedRelativeURL();

    String getClientRequestServerName();

    boolean isSecure();

    RemoteAddress getRemoteAddress();

    String getClientId();

    String getRequestId();

    Date getRequestDate();

    String getAction();

    String getQueryString();

    String getRequestHeader(String name);
}
