package com.ddl.egg.mq.enhancer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.ddl.egg.mq.enums.IgnoreRetries;
import com.ddl.egg.mq.interfaces.IMsgConsumerHandle;
import com.ddl.egg.mq.mongo.dao.ErrorMessageMongoDao;
import com.ddl.egg.mq.mongo.entity.ErrorMessageMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by lincn on 2016/8/2.
 */
public abstract class MongoEnhancerMqConsumerHandle implements IMsgConsumerHandle {

	protected final Logger logger = LoggerFactory.getLogger(MongoEnhancerMqConsumerHandle.class);

	@Autowired
	private ErrorMessageMongoDao errorMessageMongoDao;

	public Action handle(Message msg) {
		try {
			logBegin(msg);
			return handleInternal(msg);
		} catch (Exception e) {
			logger.info("[消息处理异常]topic={},msgId={}", msg.getTopic(), msg.getMsgID());
			wrapMqException(e);
			try {
				Integer exType = ErrorMessageMongo.NormalEx;
				IgnoreRetries ignoreRetries = ignoreLevel(e);
				if (IgnoreRetries.IgnoreAndReturn == ignoreRetries) {//直接返回
					return Action.CommitMessage;
				} else if (IgnoreRetries.IgnoreAndExecuteAfterHandler == ignoreRetries) {
					exType = ErrorMessageMongo.IgnoreEx;
				}
				if (IgnoreRetries.IgnoreAndExecuteAfterHandler == ignoreRetries || msg.getReconsumeTimes() >= getReconsumeLimit()) {
					afterHandle(msg, exType, e.getMessage());
					return Action.CommitMessage;
				}
			} catch (Exception afterE) {
				logger.info("[消息处理异常]后置处理异常");
				wrapMqException(afterE);
			}
			return Action.ReconsumeLater;
		} finally {
			logEnd();
		}
	}

	/**
	 * 内部处理逻辑
	 * @param msg
	 * @return
	 */
	protected abstract Action handleInternal(Message msg);

	/**
	 * 判断消息是否重试
	 * @param e handleInternal抛出的异常
	 * @return IgnoreRetries
	 */
	protected IgnoreRetries ignoreLevel(Exception e) {
		return IgnoreRetries.No;
	}

	protected void logBegin(Message msg) {
		logger.debug("[消息处理]开始");
	}

	protected void logEnd() {
		logger.debug("[消息处理]结束");
	}

	protected void handleMqException(Exception e) {
		logger.error("[消息处理]异常", e);
	}

	/**
	 * 后置处理
	 * 把消息存入mongodb
	 * @param msg
	 * @param exType 自定义异常
	 * @param errMsg 错误信息
	 */
	protected void afterHandle(Message msg, Integer exType, String errMsg) {
		ErrorMessageMongo errorMessage = new ErrorMessageMongo();
		errorMessage.setTopic(msg.getTopic());
		errorMessage.setTag(msg.getTag());
		errorMessage.setExType(exType);
		errorMessage.setErrMsg(errMsg);
		errorMessage.setCreateTime(new Date());
		try {
			errorMessage.setContent(new String(msg.getBody(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		errorMessageMongoDao.save(errorMessage);
	}

	/**
	 * 获取重试限制
	 * @return
	 */
	protected int getReconsumeLimit() {
		//1     2     3      4      5      6
		//10 秒+30 秒+1 分钟+2 分钟+3 分钟+4 分钟
		return 3;
	}

	/**
	 * 包装exception
	 * @param e
	 */
	private void wrapMqException(Exception e) {
		try {
			handleMqException(e);
		} catch (Exception loge) {
			logger.error("[消息处理异常]输出日志异常", e);
		}
	}
}
