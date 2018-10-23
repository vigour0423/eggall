package com.ddl.egg.exception;

/**
 * Created by zhuyuefan on 2016/5/10.
 */
public class CatchableException extends Exception {

    private int errorCode;

    public CatchableException(int code, String s) {
        super(s);
        errorCode = code;
    }

    public CatchableException() {
        super();
    }

    public CatchableException(int code, String message, Throwable cause) {
        super(message, cause);
        errorCode = code;
    }

    public CatchableException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
