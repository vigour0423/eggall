package com.ddl.egg.exception;

/**
 * 错误信息接口类
 *
 * @author lincn
 */
public interface ResultEnum {
    /**
     * 获取返回码:0：正常，非0：错误
     */
    public int getCode();

    /**
     * 错误信息
     */
    public String getMessage();

}