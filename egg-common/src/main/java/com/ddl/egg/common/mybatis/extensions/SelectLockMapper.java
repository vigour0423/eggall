package com.ddl.egg.common.mybatis.extensions;

import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by lincn on 2016/6/3.
 */
public interface SelectLockMapper<T> {

	/**
	 *
	 * @param key
	 * @return
	 */
	@SelectProvider(type = BaseSelectForUpdateProvider.class, method = "dynamicSQL")
	T selectByPrimaryKeyForUpdate(Object key);

	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
	 *
	 * @param record
	 * @return
	 */
	@SelectProvider(type = BaseSelectForUpdateProvider.class, method = "dynamicSQL")
	T selectOneForUpdate(T record);


}
