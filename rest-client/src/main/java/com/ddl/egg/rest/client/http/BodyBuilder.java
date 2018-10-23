package com.ddl.egg.rest.client.http;


public interface BodyBuilder extends HeaderBuilder {
    HeaderBuilder form();

    BodyBuilder xml(String body);

    BodyBuilder text(String body, String contentType);

    BodyBuilder binary(byte[] body);

    BodyBuilder chunked(boolean chunked);
}
