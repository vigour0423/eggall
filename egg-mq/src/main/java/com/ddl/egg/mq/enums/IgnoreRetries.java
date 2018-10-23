package com.ddl.egg.mq.enums;

/**
 * Created by lincn on 2016/12/15.
 */
public enum IgnoreRetries {
	No("不忽略异常重试"),
	IgnoreAndReturn("忽略异常重试并马上返回"),
	IgnoreAndExecuteAfterHandler("忽略异常重试并执行后置处理");

	private IgnoreRetries(String text){
		this.text=text;
	}
	private String text;

}
