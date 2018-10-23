package com.ddl.egg.common.dto.response;

import java.io.Serializable;

/**
 * Created by zhuyuefan on 2016/4/19.
 */
public class GenericRpcResponse<T> implements Serializable {

    //0代表成功,其他代表有异常
    protected int code;

    protected String message;

    protected T data;

    public GenericRpcResponse() {
    }

    public GenericRpcResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GenericRpcResponse(T data) {
        this.data = data;
    }

    public GenericRpcResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
