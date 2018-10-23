package com.ddl.egg.common.util;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jQuery-EasyUI:DataGrid数据表格工具类
 * 
 * @author LP
 *
 */
public abstract class DataGridUtils {
	/**
	 * Page转换为DataGrid数据格式json字符串
	 * 
	 * @param page
	 * @return
	 */
	public static <T> String PageToJson(PageInfo<T> page) {
		long total = page.getTotal();
		List<T> list = page.getList();
		String json = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			if(CollectionUtils.isEmpty(list)){
				list = Lists.newArrayList();
			}
			map.put("rows", list);
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(map);
			json = JSON.toJSONString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * List转换为DataGrid数据格式json字符串
	 * 
	 * @param list
	 * @return
	 */
	public static <T> String ListToJson(List<T> list) {
		String json = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", list.size());
			map.put("rows", list);
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(map);
			json = JSON.toJSONString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
