package com.ddl.egg.mq.mongo.dao;

import com.ddl.egg.mongo.dao.BaseMongoDao;
import com.ddl.egg.mq.mongo.entity.ErrorMessageMongo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 消息服务器重试x次以上的错误信息mongodao
 * Created by lincn on 2016/08/02.
 */
public class ErrorMessageMongoDao extends BaseMongoDao {

	public List<ErrorMessageMongo> findByTopic(String topic) {
		Query query = new Query(where("topic").is(topic)).with(new Sort(Sort.Direction.DESC, "createTime"));
		return find(query, ErrorMessageMongo.class);
	}

	public ErrorMessageMongo findOne(String _id) {
		return get(_id, ErrorMessageMongo.class);
	}

	/**
	 * 根据topic分页查询
	 * @param topic
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<ErrorMessageMongo> findPageByTopic(String topic, int page, int size) {
		Query query = new Query();
		if (StringUtils.isNotBlank(topic)) {
			query.addCriteria(where("topic").is(topic));
		}
		long count = count(query, ErrorMessageMongo.class);
		PageRequest pageRequest = new PageRequest(page - 1, size, Sort.Direction.DESC, "createTime");
		query.with(pageRequest);
		List<ErrorMessageMongo> lists = find(query, ErrorMessageMongo.class);
		return new PageImpl<ErrorMessageMongo>(lists, pageRequest, count);
	}

	/**
	 * 分页查询
	 * @param query
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<ErrorMessageMongo> findPage(Query query, int page, int size) {
		long count = count(query, ErrorMessageMongo.class);
		PageRequest pageRequest = new PageRequest(page - 1, size);
		query.with(pageRequest);
		List<ErrorMessageMongo> lists = find(query, ErrorMessageMongo.class);
		return new PageImpl<ErrorMessageMongo>(lists, pageRequest, count);
	}

	/**
	 * 通过id删除
	 * @param id
	 */
	public void deleteById(String id) {
		ErrorMessageMongo errorMessageMongo = findOne(id);
		delete(errorMessageMongo);
	}
}
