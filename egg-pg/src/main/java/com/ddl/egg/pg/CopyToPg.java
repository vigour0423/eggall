package com.ddl.egg.pg;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ddl.egg.exception.BusinessException;
import com.ddl.egg.pg.dto.CopyParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lincn on 2017/9/20.
 */
public class CopyToPg {

	protected static Logger logger = LoggerFactory.getLogger(CopyToPg.class);

	public static final String COPY_COMMAND = "COPY %s ( %s )  FROM STDIN USING DELIMITERS '%s' NULL '%s'";
	public static final String NULL_STR = "NULL";
	public static final String COPY_DELIMITER = "|";

	private DruidDataSource dataSource;

	public CopyToPg(DruidDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void batchInsert(CopyParams copyParams) {
		check(copyParams);
		String tableName = buildTableName(copyParams);
		List<String> dataLineList = Lists.newArrayList();
		for (int i = 0; i < copyParams.getDatas().size(); i++) {
			dataLineList.add(buildDataLine(copyParams.getDatas().getJSONObject(i), copyParams));
		}
		copyToPG(dataLineList, tableName, StringUtils.join(copyParams.getColumns(), ","));

	}

	private void copyToPG(List<String> dataLineList, String tableName, String columns) {
		logger.debug("start copy.");
		if (!CollectionUtils.isEmpty(dataLineList)) {
			CopyIn copyIn = null;
			try (DruidPooledConnection connection = dataSource.getConnection()) {
				PGConnection pgConnection = (PGConnection) ((ConnectionProxyImpl) connection.getConnection()).getConnectionRaw();
				CopyManager copyManager = new CopyManager((BaseConnection) pgConnection);
				String sql = String.format(COPY_COMMAND, tableName, columns, COPY_DELIMITER, NULL_STR);
				logger.debug("sql::::::::::::::::::::::::::" + sql);
				copyIn = copyManager.copyIn(sql);
				for (String row : dataLineList) {
					byte[] bytes = row.getBytes();
					copyIn.writeToCopy(bytes, 0, bytes.length);
				}
				copyIn.endCopy();
			} catch (Exception e) {
				throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "copy出错", e);
			} finally {
				if (copyIn != null && copyIn.isActive()) {
					try {
						copyIn.cancelCopy();
					} catch (SQLException e) {
						throw new BusinessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "cancelCopy出错", e);
					}
				}
				dataLineList.clear();

			}
		}
		logger.debug("end copy.");

	}

	protected void check(CopyParams copyParams) {
		if (copyParams == null) {
			throw new IllegalArgumentException("copy参数不能为空");
		}
		if (copyParams.getTableName() == null) {
			throw new IllegalArgumentException("copy命令tablename不能为空");
		}
		if (CollectionUtils.isEmpty(copyParams.getColumns())) {
			throw new IllegalArgumentException("copy命令字段集不能为空");
		}
		if (CollectionUtils.isEmpty(copyParams.getDatas())) {
			throw new IllegalArgumentException("copy命令数据集不能为空");
		}
	}

	protected String buildTableName(CopyParams copyParams) {
		if (StringUtils.isBlank(copyParams.getScheme())) {
			return copyParams.getTableName();
		} else {
			return copyParams.getScheme() + "." + copyParams.getTableName();
		}
	}

	protected String buildDataLine(JSONObject json, CopyParams copyParams) {
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		for (String column : copyParams.getColumns()) {
			if (i != 0) {
				stringBuilder.append(COPY_DELIMITER);
			}
			i++;
			Object value = json.get(column);
			if (value == null) {
				stringBuilder.append(processNullData(column, copyParams));
			} else {
				if (value instanceof String) {
					String valueStr = (String) value;
					valueStr = cleanData(valueStr);
					if (StringUtils.isBlank(valueStr)) {
						stringBuilder.append(processNullData(column, copyParams));
					} else {
						stringBuilder.append(valueStr);
					}
				} else {
					stringBuilder.append(value);
				}
			}
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	protected String processNullData(String column, CopyParams copyParams) {
		return NULL_STR;
	}

	protected String cleanData(String value) {  //todo(mark) 目前还在收集阶段。后续统一重构
		if (value == null)
			return null;
		return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\\u0000", "").replaceAll("\\|", "\\\\|");
		// .replaceAll("'", "\\\\'") 这个还未遇到过，应该会报错
	}

}
