package com.ddl.egg.common.mybatis.service.api;

/**
 * Created by zhuyuefan on 2016/3/12.
 */
public interface BaseCRUDService<T> {

	public int save(T entity);

	public int deleteByPrimaryKey(Object... keys);

	public int deleteByPrimaryKey(Object key);

	public T getByPrimaryKey(Object key);

	public int updateByPrimaryKey(T entity);

}
