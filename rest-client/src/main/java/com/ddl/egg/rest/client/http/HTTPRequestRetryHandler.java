package com.ddl.egg.rest.client.http;


import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import java.io.IOException;

/**
 * Created by mark.huang on 2016-09-24.
 */
public class HTTPRequestRetryHandler implements HttpRequestRetryHandler {

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        Args.notNull(exception, "Exception parameter");
        Args.notNull(context, "HTTP context");

        if (executionCount > 2) { //重试两次
            return false;
        }
        if (exception instanceof NoHttpResponseException) {  //这个会经常出现
            return true;
        }

        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        if (!clientContext.isRequestSent()) {    //请求未发出可以重试
            return true;
        }

        return false;
    }
}
