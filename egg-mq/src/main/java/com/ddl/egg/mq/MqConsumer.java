package com.ddl.egg.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.ons.api.*;
import com.ddl.egg.mq.interfaces.IMsgConsumerHandle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by lincn on 2016/8/2.
 */
public class MqConsumer {
	private final Logger logger = LoggerFactory.getLogger(MqConsumer.class);
	private Consumer consumer;
	private IMsgConsumerHandle msgHandle;
	private String topic;
	private String consumerId;
	private String accessKey;
	private String secretKey;

	public MqConsumer(String topic, String consumerId, String accessKey, String secretKey, IMsgConsumerHandle msgHandler) {
		this.topic = topic;
		this.consumerId = consumerId;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.msgHandle = msgHandler;
	}

	public String getTopic() {
		return topic;
	}

	public void setMsgHandle(IMsgConsumerHandle msgHandle) {
		this.msgHandle = msgHandle;
	}

	public void start() throws MQClientException {
		logger.debug("消息服务器consume start");
		if (StringUtils.isBlank(topic)) {
			throw new IllegalArgumentException("topic不能为空");
		}
		if (StringUtils.isBlank(consumerId)) {
			throw new IllegalArgumentException("consumerId不能为空");
		}
		if (StringUtils.isBlank(accessKey)) {
			throw new IllegalArgumentException("accessKey不能为空");
		}
		if (StringUtils.isBlank(secretKey)) {
			throw new IllegalArgumentException("secretKey不能为空");
		}
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ConsumerId, consumerId);
		properties.put(PropertyKeyConst.AccessKey, accessKey);
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		consumer = ONSFactory.createConsumer(properties);
		consumer.subscribe(topic, "*", new MessageListener() {
			public Action consume(Message message, ConsumeContext conwtext) {
				return msgHandle.handle(message);
			}
		});
		consumer.start();
		logger.debug("消息服务器consume end");
	}

	public void shutdown() {
		if (consumer != null) {
			consumer.shutdown();
		}
	}
}
