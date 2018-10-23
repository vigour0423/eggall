package com.ddl.egg.web.platform.exception;


import com.ddl.egg.log.exception.Warning;

@Warning
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
