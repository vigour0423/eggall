package com.ddl.egg.rest.client;

import com.ddl.egg.jaxb.JAXB;
import com.ddl.egg.json.JSON;
import com.ddl.egg.log.util.CharacterEncodings;
import com.ddl.egg.rest.client.api.APIResponseProcessor;
import com.ddl.egg.rest.client.exception.RemoteServiceException;
import com.ddl.egg.rest.client.http.HTTPClient;
import com.ddl.egg.rest.client.http.HTTPConstants;
import com.ddl.egg.rest.client.http.HTTPRequest;
import com.ddl.egg.rest.client.http.HTTPResponse;
import com.ddl.egg.rest.client.http.HTTPStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class RESTServiceClient {
    private final Logger logger = LoggerFactory.getLogger(RESTServiceClient.class);
    private String requestId;
    private HTTPClient httpClient;
    private boolean validateStatusCode;

    public <T> T get(String uri, Class<T> responseClass, Class<? extends APIResponseProcessor> apiResponseProcessor, int timeout, String contentType) {
        return executeAndReturn(HTTPRequest.get(uri).request(), responseClass, apiResponseProcessor, timeout, contentType);
    }

    @SuppressWarnings("unchecked")
    public <T, U> T post(String uri, Class<T> responseClass, U request, Class<? extends APIResponseProcessor> apiResponseProcessor, int timeout, String contentType) {
        HTTPRequest httpRequest = toRequest(uri, request, contentType);
        return executeAndReturn(httpRequest, responseClass, apiResponseProcessor, timeout, contentType);
    }

    @SuppressWarnings("unchecked")
    public <T, U> T put(String uri, Class<T> responseClass, U request, Class<? extends APIResponseProcessor> apiResponseProcessor, int timeout, String contentType) {
        HTTPRequest httpRequest = toRequest(uri, request, contentType);
        return executeAndReturn(httpRequest, responseClass, apiResponseProcessor, timeout, contentType);
    }

    public <T> T delete(String uri, Class<T> responseClass, Class<? extends APIResponseProcessor> apiResponseProcessor, int timeout, String contentType) {
        return executeAndReturn(HTTPRequest.delete(uri).request(), responseClass, apiResponseProcessor, timeout, contentType);
    }

    private HTTPRequest toRequest(String uri, Object request, String contentType) {
        HTTPRequest httpRequest;
        if (HTTPConstants.CONTENT_TYPE_XML.equals(contentType)) {
            String xml = request instanceof String ? (String) request : JAXB.toXml(request, CharacterEncodings.UTF_8, true);
            httpRequest = HTTPRequest.post(uri).xml(xml).request();
        } else if (HTTPConstants.CONTENT_TYPE_FORM.equals(contentType)) {
            httpRequest = HTTPRequest.post(uri).text((String) request, HTTPConstants.CONTENT_TYPE_FORM).request();
        } else {
            httpRequest = HTTPRequest.post(uri).text(JSON.toJSON(request), HTTPConstants.CONTENT_TYPE_JSON).request();
        }

        return httpRequest;
    }

    @SuppressWarnings("unchecked")
    private <T> T executeAndReturn(HTTPRequest request, Class<T> responseClass, Class<? extends APIResponseProcessor> apiResponseProcessor, int timeout, String contentType) {
        prepareRequestHeaders(request);
        HTTPResponse response = httpClient.execute(request, timeout);

        if (validateStatusCode) {
            validateStatusCode(request, response);
        }

        if (Void.TYPE.equals(responseClass)) {
            return null;
        }

        String responseText = response.responseText();
        if (apiResponseProcessor != null) {
            responseText = processResponse(request, response, apiResponseProcessor);
        }

        if (HTTPConstants.CONTENT_TYPE_XML.equals(contentType)) {
            return (T) JAXB.toObject(responseText, responseClass);
        } else if (HTTPConstants.CONTENT_TYPE_FORM.equals(contentType)) {
            return (T) responseText;
        } else {
            return JSON.fromJSON(responseText, responseClass);
        }
    }

    private String processResponse(HTTPRequest request, HTTPResponse response, Class<? extends APIResponseProcessor> apiResponseProcessors) {
        try {
            logger.debug("process Response => {}", apiResponseProcessors.getName());
            return apiResponseProcessors.newInstance().process(response);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("failed to process Response.", e);
        } catch (RuntimeException e) {
            logger.error("failed to {} {} with request : {}", request.method(), request.url(), request.body());
            throw e;
        }
        return null;
    }

    private void prepareRequestHeaders(HTTPRequest request) {
        request.accept(HTTPConstants.CONTENT_TYPE_JSON);
    }


    private void validateStatusCode(HTTPRequest request, HTTPResponse response) {
        HTTPStatusCode statusCode = response.statusCode();
        if (statusCode.isSuccess()) {
            return;
        }
        logger.debug("response status code => {}", statusCode.code());

        throw new RemoteServiceException("failed to execute service, request is " + request.body() + ", responseText=" + response.responseText() + ", statusCode=" + statusCode.code(), statusCode.code());
    }

    String getRequestId() {
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setValidateStatusCode(boolean validateStatusCode) {
        this.validateStatusCode = validateStatusCode;
    }

    @Autowired
    public void setHTTPClient(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }
}
