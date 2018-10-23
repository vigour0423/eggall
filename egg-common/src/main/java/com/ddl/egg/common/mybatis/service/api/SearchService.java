package com.ddl.egg.common.mybatis.service.api;

import java.util.List;
import java.util.Map;

/**
 * Created by lincn on 2016/5/19.
 */
public interface SearchService<T> {

	public  List<T> query();

	public List<T> query(Map<String, Object> params) ;

	public List<T> query(Map<String, Object> params, String orderByClause);

}
