package com.ddl.egg.mq;

import com.ddl.egg.mq.enhancer.MongoEnhancerMqConsumerHandle;

/**
 * Created by lincn on 2016/10/26.
 */
public class ConsumerChannel {

	public ConsumerChannel(MongoEnhancerMqConsumerHandle msgHandler, String topic) {
		this.msgHandler = msgHandler;
		this.topic = topic;
	}

	private MongoEnhancerMqConsumerHandle msgHandler;
	private String topic;

	public MongoEnhancerMqConsumerHandle getMsgHandler() {
		return msgHandler;
	}

	public void setMsgHandler(MongoEnhancerMqConsumerHandle msgHandler) {
		this.msgHandler = msgHandler;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
