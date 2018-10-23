package com.ddl.egg.rest.client.http;


public class HTTPException extends RuntimeException {
    public HTTPException(String message) {
        super(message);
    }

    public HTTPException(Throwable cause) {
        super(cause);
    }
}
