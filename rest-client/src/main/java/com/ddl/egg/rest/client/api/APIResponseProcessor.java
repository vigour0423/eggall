package com.ddl.egg.rest.client.api;


import com.ddl.egg.rest.client.http.HTTPResponse;

public abstract class APIResponseProcessor {

    public abstract String process(final HTTPResponse httpResponse);

}
