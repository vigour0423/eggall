package com.ddl.egg.pg.dto;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * Created by XPS-15 on 2017/9/20.
 */
public interface CopyParams {

	public String getScheme() ;

	public void setScheme(String scheme);

	public String getTableName() ;

	public void setTableName(String tableName) ;

	public List<String> getColumns() ;

	public void setColumns(List<String> columns) ;

	public JSONArray getDatas() ;

	public void setDatas(JSONArray datas) ;
}
