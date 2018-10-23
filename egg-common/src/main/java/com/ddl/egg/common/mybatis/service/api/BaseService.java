package com.ddl.egg.common.mybatis.service.api;


/**
 * 公共基础服务接口，集成所有接口
 * <p>
 * Created by LuoPeng on 2016-05-03.
 */
public interface BaseService<T> extends BaseQueryService<T>, BaseOperateService<T>, BaseDeleteService<T>, BaseCRUDService<T>, PageService<T>, SearchService<T> {

}
