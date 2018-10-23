package com.ddl.egg.common.mybatis.service.api;

import com.github.pagehelper.PageInfo;
import com.ddl.egg.common.dto.request.PageRequest;

import java.util.Map;

/**
 * Created by lincn on 2016/4/22.
 */
public interface PageService<T> {

    public PageInfo<T> selectPaged(PageRequest page, Map<String, Object> params);

}
