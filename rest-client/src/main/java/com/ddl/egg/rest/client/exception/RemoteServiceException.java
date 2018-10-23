package com.ddl.egg.rest.client.exception;


public class RemoteServiceException extends RuntimeException {

    private final int statusCode;

    public RemoteServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
