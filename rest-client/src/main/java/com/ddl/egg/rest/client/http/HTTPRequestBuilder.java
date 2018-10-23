package com.ddl.egg.rest.client.http;

import com.ddl.egg.log.util.CharacterEncodings;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


final class HTTPRequestBuilder extends HTTPRequest implements ParameterBuilder, BodyBuilder, HeaderBuilder {
    private final String url;

    private final HTTPMethod method;

    private List<NameValuePair> parameters;

    private HTTPHeaders headers;

    private AbstractHttpEntity entity;

    private boolean chunked;

    private String contentType = HTTPConstants.CONTENT_TYPE_PLAIN;

    private boolean formRequest;

    HTTPRequestBuilder(String url, HTTPMethod method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public List<NameValuePair> parameters() {
        return parameters;
    }

    @Override
    public AbstractHttpEntity getEntity() {
        return entity;
    }

    @Override
    public HTTPMethod method() {
        return method;
    }

    @Override
    public HeaderBuilder addHeader(String key, String value) {
        if (headers == null)
            headers = new HTTPHeaders();
        headers.add(key, value);
        return this;
    }

    @Override
    public HeaderBuilder accept(String contentType) {
        addHeader(HTTPConstants.HEADER_ACCEPT, contentType);
        return this;
    }

    @Override
    public HTTPHeaders headers() {
        return headers;
    }

    @Override
    public String url() {
        if (!formRequest && parameters != null) {
            String queryChar = url.contains("?") ? "&" : "?";
            return url + queryChar + URLEncodedUtils.format(parameters, CharacterEncodings.CHARSET_ISO_8859_1);
        }
        return url;
    }

    @Override
    public HTTPRequestBuilder request() {
        return this;
    }

    @Override
    public HTTPRequestBuilder addParameter(String key, String value) {
        if (parameters == null) {
            parameters = new ArrayList<>();
        }
        parameters.add(new BasicNameValuePair(key, value));
        return this;
    }

    @Override
    public HTTPRequestBuilder setParameter(String key, String value) {
        if (parameters != null)
            for (Iterator<NameValuePair> iterator = parameters.listIterator(); iterator.hasNext(); ) {
                NameValuePair param = iterator.next();
                if (param.getName().equals(key))
                    iterator.remove();
            }
        addParameter(key, value);
        return this;
    }

    @Override
    public HTTPRequestBuilder chunked(boolean chunked) {
        this.chunked = chunked;
        return this;
    }

    @Override
    public HTTPRequestBuilder text(String body, String contentType) {
        Assert.isNull(entity, "Entity already set");
        entity = new StringEntity(body, CharacterEncodings.CHARSET_UTF_8);
        this.contentType = contentType;
        return this;
    }

    @Override
    public BodyBuilder form() {
        Assert.isNull(entity, "Entity already set");
        this.formRequest = true;
        this.contentType = HTTPConstants.CONTENT_TYPE_FORM;
        return this;
    }

    @Override
    public BodyBuilder xml(String body) {
        Assert.isNull(entity, "Entity already set");
        return text(body, HTTPConstants.CONTENT_TYPE_XML);
    }

    @Override
    public BodyBuilder binary(byte[] bytes) {
        Assert.isNull(entity, "Entity already set");
        entity = new ByteArrayEntity(bytes);
        entity.setContentType("binary/octet-stream");
        return this;
    }

    @Override
    public String body() {
        String body;
        switch (method) {
            case POST:
            case PUT:
                if (formRequest) {
                    initFormEntity();
                }
                if (!isMultipart()) {
                    try {
                        body = EntityUtils.toString(entity, CharacterEncodings.CHARSET_UTF_8);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                } else {
                    body = "";
                }
                break;
            default:
                body = "";
        }

        return body;
    }

    boolean isMultipart() {
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    @Override
    public HttpRequestBase getRawRequest() {
        HttpRequestBase request;

        switch (method) {
            case GET:
                request = new HttpGet(url());
                break;
            case POST:
                if (formRequest) {
                    initFormEntity();
                }

                Assert.notNull(entity, "Entity not set");

                entity.setChunked(chunked);
                // TODO(mark.huang): support charset?
                entity.setContentType(contentType + "; charset=UTF-8");

                HttpPost post = new HttpPost(url());
                post.setEntity(entity);
                request = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(url());
                entity.setChunked(chunked);
                entity.setContentType(contentType + "; charset=UTF-8");
                put.setEntity(entity);
                request = put;
                break;
            case DELETE:
                request = new HttpDelete(url());
                break;
            default:
                throw new HTTPException("unknown method, method=" + method);
        }

        prepareRequest(request);
        return request;
    }

    void prepareRequest(HttpRequestBase request) {
        if (headers != null)
            for (HTTPHeader header : headers) {
                request.addHeader(new BasicHeader(header.name(), header.value()));
            }
    }

    void initFormEntity() {
        if (entity == null) {
            entity = new UrlEncodedFormEntity(parameters, CharacterEncodings.CHARSET_UTF_8);
        }
    }
}
