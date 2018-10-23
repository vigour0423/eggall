package com.ddl.egg.web.platform.exception;

/**
 * Created by mark.huang on 2016-09-07.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
