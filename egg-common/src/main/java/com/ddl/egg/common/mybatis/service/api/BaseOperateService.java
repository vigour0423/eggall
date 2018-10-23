package com.ddl.egg.common.mybatis.service.api;


/**
 * 公共基础操作服务接口
 *
 * Created by LuoPeng on 2016-05-03.
 */
public interface BaseOperateService<T>{

    /**
     * 创建实体（插入）
     * @param entity
     * @return
     */
    public int create(T entity);

    /**
     * 修改实体
     * @param entity
     * @return
     */
    public int update(T entity);

}
