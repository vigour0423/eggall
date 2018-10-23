package com.ddl.egg.exception;

import java.util.UUID;

/**
 * Created by zhuyuefan on 2016/5/4.
 */
public class BusinessException extends RuntimeException {

    private int errorCode;
    /**
     *
     */
    private static final long serialVersionUID = -2580863687548674587L;

    private String token = UUID.randomUUID().toString();

    public BusinessException(int code, String s) {
        super(s);
        errorCode = code;
    }

    public BusinessException() {
        super();
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        errorCode = code;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        errorCode = resultEnum.getCode();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
