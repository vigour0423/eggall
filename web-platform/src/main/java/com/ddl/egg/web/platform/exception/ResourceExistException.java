package com.ddl.egg.web.platform.exception;


import com.ddl.egg.log.exception.Ignore;

@Ignore
public class ResourceExistException extends RuntimeException {
    public ResourceExistException(String message) {
        super(message);
    }

    public ResourceExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
