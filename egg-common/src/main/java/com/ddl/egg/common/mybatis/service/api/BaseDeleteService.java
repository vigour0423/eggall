package com.ddl.egg.common.mybatis.service.api;


/**
 * 公共基础删除服务接口
 *
 * Created by LuoPeng on 2016-10-29.
 */
public interface BaseDeleteService<T>{

    /**
     * 删除实体
     * @param entity
     * @return
     */
    public int delete(T entity);

    /**
     * 删除实体
     * @param entities
     * @return
     */
    public int delete(Iterable<? extends T> entities);

    /**
     * 删除实体
     * @param key
     * @return
     */
    public int deleteByPrimaryKey(Object key);

    /**
     *
     * @param keys
     * @return
     */
    public int deleteByPrimaryKey(Object... keys);

}
