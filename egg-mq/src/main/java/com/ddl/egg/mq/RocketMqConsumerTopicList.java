package com.ddl.egg.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.ons.api.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * Created by lincn on 2016/10/26.
 */
public class RocketMqConsumerTopicList {
	private final Logger logger = LoggerFactory.getLogger(RocketMqConsumerTopicList.class);
	private Consumer consumer;
	private String consumerId;
	private String accessKey;
	private String secretKey;
	private List<ConsumerChannel> consumerChannels;

	public RocketMqConsumerTopicList(String consumerId, String accessKey, String secretKey) {
		this.consumerId = consumerId;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public void start() throws MQClientException {
		logger.debug("消息服务器consumeList start");
		if (consumerChannels == null || consumerChannels.isEmpty()) {
			throw new IllegalArgumentException("consumerDtos不能为空");
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
		for (ConsumerChannel consumerDto : consumerChannels) {
			if (StringUtils.isBlank(consumerDto.getTopic())) {
				throw new IllegalArgumentException("topic不能为空");
			}
			logger.debug("消息服务器consume:{}", consumerDto.getTopic());
			consumer.subscribe(consumerDto.getTopic(), "*", new MessageListener() {
				public Action consume(Message message, ConsumeContext conwtext) {
					return consumerDto.getMsgHandler().handle(message);
				}
			});
		}
		consumer.start();
		logger.debug("消息服务器consumeList end");
	}

	public void shutdown() {
		if (consumer != null) {
			consumer.shutdown();
		}
	}

	public List<ConsumerChannel> getConsumerChannels() {
		return consumerChannels;
	}

	public void setConsumerChannels(List<ConsumerChannel> consumerChannels) {
		this.consumerChannels = consumerChannels;
	}
}
