package com.ddl.egg.common.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 返回结果
 */
public class BackendResult implements Serializable{
	/**
	 * 结果码：成功=0
	 */
	public static final int CODE_SUCCESS = 0;
	/**
	 * 结果码：错误=-1
	 */
	public static final int CODE_ERROR = -1;

	
	/**
	 * 结果码：0=成功
	 */
	private Integer code;
	/**
	 * 返回信息
	 */
	private String message;
	/**
	 * 返回数据
	 */
	private Object data;

	public BackendResult() {

	}

	public BackendResult(int code, String message) {
		this.setCode(code);
		this.setMessage(message);
		this.setData(null);
	}

	public BackendResult(int code, String message, Object data) {
		this.setCode(code);
		this.setMessage(message);
		this.setData(data);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static String create(int code, String message) {
		return new BackendResult(code, message, null).toJSONString();
	}

	public static String create(int code, String message, Object data) {
		return new BackendResult(code, message, data).toJSONString();
	}

	public static String error(String message, Object data) {
		return new BackendResult(CODE_ERROR, message, data).toJSONString();
	}

	public static String error(String message) {
		return new BackendResult(CODE_ERROR, message, "").toJSONString();
	}

	public static String success(String message, Object data) {
		return new BackendResult(CODE_SUCCESS, message, data).toJSONString();
	}

	public static String success(String message) {
		return new BackendResult(CODE_SUCCESS, message, "").toJSONString();
	}

	public static String success(Object data) {
		return new BackendResult(CODE_SUCCESS, "", data).toJSONString();
	}

	/**
	 * 转化为json字符串
	 * @return
     */
	public String toJSONString() {
		return JSON.toJSONString(this);
	}
}
