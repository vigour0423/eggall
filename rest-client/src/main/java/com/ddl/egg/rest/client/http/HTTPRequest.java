package com.ddl.egg.rest.client.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;

import java.util.List;

/**
 * Abstract class which you can use the static methods
 * to construct HTTPRequest or extends this class to
 * create a customize HTTPRequest
 *
 * @author anonymous
 */
public abstract class HTTPRequest {

    /**
     * Construct a GET request
     */
    public static HeaderBuilder get(String url) {
        return new HTTPRequestBuilder(url, HTTPMethod.GET);
    }

    /**
     * Construct a POST request
     */
    public static BodyBuilder post(String url) {
        return new HTTPRequestBuilder(url, HTTPMethod.POST);
    }

    /**
     * Construct a PUT request
     */
    public static BodyBuilder put(String url) {
        return new HTTPRequestBuilder(url, HTTPMethod.PUT);
    }

    /**
     * Construct a DELETE request
     */
    public static HeaderBuilder delete(String url) {
        return new HTTPRequestBuilder(url, HTTPMethod.DELETE);
    }

    public abstract HTTPMethod method();

    public abstract String body();

    public abstract String url();

    public abstract HeaderBuilder addHeader(String key, String value);

    public abstract HeaderBuilder accept(String contentType);

    abstract HTTPHeaders headers();

    abstract List<NameValuePair> parameters();

    abstract HttpRequestBase getRawRequest();

    abstract AbstractHttpEntity getEntity();
}
