package com.ddl.egg.mq.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 消息服务器重试x次以上的错误信息mongodao
 * Created by lincn on 2016/08/02.
 */
@Document(collection = "errorMessage")
public class ErrorMessageMongo {

	/**
	 * 普通异常，就是非自定义异常的异常
	 */
	public static final int NormalEx = 0;

	/**
	 * 自定义异常
	 */
	public static final int IgnoreEx = 1;

	@Id
	private String id;

	@Field("exType")
	@Indexed
	private Integer exType = NormalEx;

	@Field("errMsg")
	private String errMsg;

	@Field("topic")
	@Indexed
	private String topic;

	@Field("tag")
	private String tag;

	@Field("content")
	private String content;

	@Field("createTime")
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Integer getExType() {
		return exType;
	}

	public void setExType(Integer exType) {
		this.exType = exType;
	}

	@Override
	public String toString() {
		return "ErrorMessageMongo{" +
				"id='" + id + '\'' +
				", exType=" + exType +
				", errMsg='" + errMsg + '\'' +
				", topic='" + topic + '\'' +
				", tag='" + tag + '\'' +
				", content='" + content + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
