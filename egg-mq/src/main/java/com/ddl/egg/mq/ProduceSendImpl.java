package com.ddl.egg.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.ddl.egg.mq.interfaces.IProduceSend;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by lincn on 2016/8/2.
 */
public class ProduceSendImpl implements IProduceSend {

	private final Logger logger = LoggerFactory.getLogger(ProduceSendImpl.class);
	private Producer producer;
	private int delayTime = 10;//毫秒
	private String producerId = null;
	private String accessKey = null;
	private String secretKey = null;

	public ProduceSendImpl(String producerId, String accessKey, String secretKey) {
		this.producerId = producerId;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public ProduceSendImpl(String producerId, String accessKey, String secretKey, int delayTime) {
		this.producerId = producerId;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.delayTime = delayTime;
	}

	/**
	 *
	 * @param body
	 * @param topic
	 */
	public void send(String body, String topic) {
		send(body, topic, delayTime, null);
	}

	/**
	 *
	 * @param body
	 * @param topic
	 * @param tag
	 */
	public void send(String body, String topic, String tag) {
		send(body, topic, delayTime, tag);
	}

	/**
	 * 发送消息
	 * @param body
	 * @param topic
	 * @param delayTime 毫秒
	 */
	public void send(String body, String topic, int delayTime) {
		send(body, topic, delayTime, null);
	}

	public void send(String body, String topic, String tag, String msgKey) {
		send(body, topic, delayTime, tag, msgKey);
	}

	/**
	 * 发送消息
	 * @param body
	 * @param topic
	 * @param delayTime
	 * @param tag
	 * @param msgKey
	 */
	public void send(String body, String topic, int delayTime, String tag, String msgKey) {
		try {
			if (StringUtils.isBlank(topic)) {
				throw new IllegalArgumentException("topic不能为空");
			}
			if (StringUtils.isBlank(body)) {
				throw new IllegalArgumentException("body不能为空值");
			}
			Message msg = new Message();
			if (!StringUtils.isBlank(msgKey)) {
				msg.setKey(msgKey);
			}
			msg.setTopic(topic);
			msg.setBody(body.getBytes("UTF-8"));
			if (delayTime > 0) {
				msg.setStartDeliverTime(System.currentTimeMillis() + delayTime);
			}
			if (StringUtils.isNotBlank(tag)) {
				msg.setTag(tag);
			}
			producer.send(msg);
		} catch (Exception e) {
			throw new RuntimeException("发送消息异常",e);
		}
	}

	/***
	 * 发送消息
	 * @param body
	 * @param topic
	 * @param delayTime 毫秒
	 * @param tag
	 */
	public void send(String body, String topic, int delayTime, String tag) {
		send(body, topic, delayTime, tag, null);
	}

	public void start() throws MQClientException {
		logger.debug("消息服务器produce start");
		if (StringUtils.isBlank(producerId)) {
			throw new IllegalArgumentException("producerId不能为空");
		}
		if (StringUtils.isBlank(accessKey)) {
			throw new IllegalArgumentException("accessKey不能为空");
		}
		if (StringUtils.isBlank(secretKey)) {
			throw new IllegalArgumentException("secretKey不能为空");
		}
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ProducerId, producerId);
		properties.put(PropertyKeyConst.AccessKey, accessKey);
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		this.producer = ONSFactory.createProducer(properties);
		this.producer.start();
		logger.debug("消息服务器produce end");
	}

	public void shutdown() {
		this.producer.shutdown();
	}
}
