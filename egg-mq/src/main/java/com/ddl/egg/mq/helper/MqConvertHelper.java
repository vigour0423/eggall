package com.ddl.egg.mq.helper;

import com.alibaba.fastjson.JSON;

/**
 * Created by lincn on 2016/11/10.
 */
public class MqConvertHelper {

	public static <T> T convertBody(byte body[], Class tClass) {
		return JSON.parseObject(body, tClass);
	}
}
