package com.ddl.egg.rest.client.http;


public interface ParameterBuilder extends RequestBuilder {
    ParameterBuilder addParameter(String key, String value);

    ParameterBuilder setParameter(String key, String value);
}
