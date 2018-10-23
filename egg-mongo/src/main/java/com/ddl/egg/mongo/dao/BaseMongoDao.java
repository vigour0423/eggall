package com.ddl.egg.mongo.dao;


import com.ddl.egg.mongo.util.MongoUtils;
import com.ddl.egg.mongo.access.BulkWriteInvoker;
import com.ddl.egg.mongo.access.MongoAccess;
import com.mongodb.BulkWriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@SuppressWarnings("unchecked")
public abstract class BaseMongoDao {

    @Autowired
    protected MongoAccess mongoAccess;

    public <T> T get(Object id, Class<T> entityClass, String... includeKeys) {
        Query query = new Query(where("_id").is(id));
        MongoUtils.addIncludeKeys(query, includeKeys);
        return mongoAccess.findOne(query, entityClass);
    }

    public <T> List<T> find(Query query, Class<T> entityClass, String... includeKeys) {
        MongoUtils.addIncludeKeys(query, includeKeys);
        return mongoAccess.find(query, entityClass);
    }

    public <T> List<T> findByIds(List<?> ids, Class<T> entityClass, String... includeKeys) {
        if (CollectionUtils.isEmpty(ids)) return Collections.EMPTY_LIST;
        Query query = new Query(where("_id").in(ids));
        MongoUtils.addIncludeKeys(query, includeKeys);
        return mongoAccess.find(query, entityClass);
    }

    public long count(Query query, Class<?> entityClass) {
        return mongoAccess.count(query, entityClass);
    }

    public void save(Object entity) {
        mongoAccess.save(entity);
    }

    public void batchInsert(Collection<? extends Object> entities, Class<?> entityClass) {
        mongoAccess.batchInsert(entities, entityClass);
    }

    public void delete(Object entity) {
        mongoAccess.delete(entity);
    }

    public <T> void updateById(Update update, Object id, Class<T> entityClass) {
        mongoAccess.updateFirst(new Query().addCriteria(where("_id").is(id)), update, entityClass);
    }

    public <T> void updateByIds(Update update, List<Integer> ids, Class<T> entityClass) {
        mongoAccess.updateMulti(new Query().addCriteria(where("_id").in(ids)), update, entityClass);
    }

    public BulkWriteResult bulkWrite(Class<?> entityClass, final BulkWriteInvoker bulkWriteInvoker) {
        return mongoAccess.bulkWrite(entityClass, bulkWriteInvoker);
    }
}
