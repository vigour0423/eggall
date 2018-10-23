package com.ddl.egg.common.mybatis.service.api;


import com.ddl.egg.common.mybatis.domain.Page;
import com.ddl.egg.common.mybatis.domain.PageRequest;
import com.ddl.egg.common.mybatis.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * 公共查询基础服务接口
 * <p>
 * Created by LuoPeng on 2016-05-03.
 */
public interface BaseQueryService<T> {

    /**
     * 获取实体
     *
     * @param key 主键
     * @return
     */
    public T getEntity(Object key);

    /**
     * 查询全部实体
     *
     * @return
     */
    public List<T> findAll();

    /**
     * 条件查询List
     *
     * @param searchParams 查询参数
     * @return
     */
    public List<T> findList(Map<String, Object> searchParams);

    /**
     * 条件查询List
     *
     * @param searchParams 查询参数
     * @param sort         排序条件
     * @return
     */
    public List<T> findList(Map<String, Object> searchParams, Sort sort);

    /**
     * 条件查询Page
     *
     * @param pageRequest  分页请求
     * @param searchParams 查询参数
     * @return
     */
    public Page<T> findPage(PageRequest pageRequest, Map<String, Object> searchParams);

    /**
     * 统计所有数量
     *
     * @param
     * @return
     */
    public int countAll();

    /**
     * 统计数量
     *
     * @param
     * @return
     */
    public int count(T t);

    /**
     * 统计数量
     *
     * @param searchParams
     * @return
     */
    public int count(Map<String, Object> searchParams);
}
