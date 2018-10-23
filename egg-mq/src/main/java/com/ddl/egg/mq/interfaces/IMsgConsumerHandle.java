package com.ddl.egg.mq.interfaces;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;

/**
 * Created by admin on 2016/8/2.
 */
public interface IMsgConsumerHandle {

	public Action handle(Message msg);
}
