package com.ddl.egg.pg.dto;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * Created by XPS-15 on 2017/9/20.
 */
public class DefaultCopyParams implements CopyParams {

	private String scheme;

	private String tableName;

	private List<String> columns;

	private JSONArray datas;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public JSONArray getDatas() {
		return datas;
	}

	public void setDatas(JSONArray datas) {
		this.datas = datas;
	}
}
