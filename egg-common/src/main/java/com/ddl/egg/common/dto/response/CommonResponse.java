package com.ddl.egg.common.dto.response;

import java.io.Serializable;

/**
 * Created by zhuyuefan on 2016/4/19.
 */
public class CommonResponse implements Serializable {
    public static int CODE_SUCCESS = 0;
    public static String MSG_SUCCESS = "SUCCESS";
    //0代表成功,其他代表有异常
    protected int code;

    protected String message;

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse() {
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
