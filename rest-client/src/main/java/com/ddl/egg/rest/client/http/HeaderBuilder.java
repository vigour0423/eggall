package com.ddl.egg.rest.client.http;


public interface HeaderBuilder extends ParameterBuilder {
    HeaderBuilder addHeader(String key, String value);

    HeaderBuilder accept(String accept);
}
