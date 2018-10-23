package com.ddl.egg.common.mybatis.extensions;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * Created by lincn on 2016/6/3.
 */
public class BaseSelectForUpdateProvider extends MapperTemplate {

	public BaseSelectForUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 查询
	 *
	 * @param ms
	 * @return
	 */
	public String selectOneForUpdate(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		//修改返回值类型为实体类型
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
		sql.append(LOCKSQL);
		return sql.toString();
	}

	/**
	 * 根据主键进行查询
	 *
	 * @param ms
	 */
	public String selectByPrimaryKeyForUpdate(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		//将返回值修改为实体类型
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.wherePKColumns(entityClass));
		sql.append(LOCKSQL);
		return sql.toString();
	}


	private final String LOCKSQL=" for update";

}
