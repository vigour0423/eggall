package com.ddl.egg.mq.transaction;

import com.ddl.egg.common.transaction.extension.StrategyParams;

/**
 * Created by lincn on 2018/1/10.
 */
public class MqStrategyParams implements StrategyParams {

	public MqStrategyParams(String body, String topic) {
		this.body = body;
		this.topic = topic;
	}

	public MqStrategyParams(String body, String topic, String msgKey) {
		this.body = body;
		this.topic = topic;
		this.msgKey = msgKey;
	}

	public MqStrategyParams(String body, String topic, String msgKey, int delayTime) {
		this.body = body;
		this.topic = topic;
		this.msgKey = msgKey;
		this.delayTime = delayTime;
	}

	public MqStrategyParams(String body, String topic, String msgKey, String tag) {
		this.body = body;
		this.topic = topic;
		this.msgKey = msgKey;
		this.tag = tag;
	}

	String body;
	String topic;
	int delayTime = 1;
	String tag;
	String msgKey;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
}
