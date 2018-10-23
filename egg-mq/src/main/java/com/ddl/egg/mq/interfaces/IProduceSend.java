package com.ddl.egg.mq.interfaces;

/**
 * Created by admin on 2016/8/2.
 */
public interface IProduceSend {

	/**
	 *
	 * @param body
	 * @param topic
	 */
	public void send(String body, String topic);

	/**
	 *
	 * @param body
	 * @param topic
	 * @param tag
	 */
	public void send(String body, String topic, String tag);


	/**
	 * 发送消息
	 * @param body
	 * @param topic
	 * @param delayTime 毫秒
	 */
	public void send(String body, String topic, int delayTime);


	public void send(String body, String topic, String tag, String msgKey);

	/***
	 * 发送消息
	 * @param body
	 * @param topic
	 * @param delayTime 毫秒
	 * @param tag
	 */
	public void send(String body, String topic, int delayTime, String tag);

	public void send(String body, String topic, int delayTime, String tag, String msgKey);
}
