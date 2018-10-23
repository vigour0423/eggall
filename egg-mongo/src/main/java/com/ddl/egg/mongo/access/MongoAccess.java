package com.ddl.egg.mongo.access;

import com.ddl.egg.log.util.StopWatch;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

/**
 * @author mark.huang
 */
public class MongoAccess {

    private final Logger logger = LoggerFactory.getLogger(MongoAccess.class);

    private MongoTemplate mongoTemplate;

    public <T> T get(Object id, Class<T> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.findById(id, entityClass);
        } finally {
            logger.debug("get, id={},  entityClass={}, elapsedTime={}", id, entityClass.getName(), watch.elapsedTime());
        }
    }

    public <T> List<T> find(Query query, Class<T> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.find(query, entityClass);
        } finally {
            logger.debug("find by Query, query={}, entityClass={}, elapsedTime={}", query, entityClass.getName(), watch.elapsedTime());
        }
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.findAll(entityClass);
        } finally {
            logger.debug("findAll, entityClass={}, elapsedTime={}", entityClass.getName(), watch.elapsedTime());
        }
    }

    public <T> List<T> findAll(Class<T> entityClass, String collection) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.findAll(entityClass, collection);
        } finally {
            logger.debug("findAll, entityClass={}, elapsedTime={}", entityClass.getName(), watch.elapsedTime());
        }
    }

    public <T> T findOne(Query query, Class<T> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.findOne(query, entityClass);
        } finally {
            logger.debug("findOne by Query, query={}, entityClass={}, elapsedTime={}", query, entityClass, watch.elapsedTime());
        }
    }

    public void save(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            mongoTemplate.save(entity);
        } finally {
            logger.debug("save, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void batchInsert(Collection<? extends Object> entities, Class<?> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            mongoTemplate.insert(entities, entityClass);
        } finally {
            logger.debug("batchInsert, size={}, entityClass={}, elapsedTime={}", entities.size(), entityClass.getName(), watch.elapsedTime());
        }
    }

    public void batchInsertAll(Collection<? extends Object> entities) {
        StopWatch watch = new StopWatch();
        try {
            mongoTemplate.insertAll(entities);
        } finally {
            logger.debug("insertAll, size={}, elapsedTime={}", entities.size(), watch.elapsedTime());
        }
    }

    public WriteResult updateFirst(Query query, Update update, Class entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.updateFirst(query, update, entityClass);
        } finally {
            logger.debug("updateFirst,query={}, update={}, entityClass={}, elapsedTime={}", query, update, entityClass.getName(), watch.elapsedTime());
        }
    }

    public WriteResult updateMulti(Query query, Update update, Class<?> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.updateMulti(query, update, entityClass);
        } finally {
            logger.debug("updateMulti,query={}, update={}, entityClass={}, elapsedTime={}", query, update, entityClass.getName(), watch.elapsedTime());
        }
    }

    public WriteResult upsert(Query query, Update update, Class<?> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.upsert(query, update, entityClass);
        } finally {
            logger.debug("upsert,query={}, update={}, entityClass={}, elapsedTime={}", query, update, entityClass.getName(), watch.elapsedTime());
        }
    }

    public void delete(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            mongoTemplate.remove(entity);
        } finally {
            logger.debug("delete, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public WriteResult delete(Query query, Class<?> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.remove(query, entityClass);
        } finally {
            logger.debug("delete, query={}, entityClass={}, elapsedTime={}", query, entityClass.getName(), watch.elapsedTime());
        }
    }

    public <T> void batchDelete(List<Integer> ids, Class<T> entityClass) {
        StopWatch watch = new StopWatch();
        Query query = Query.query(Criteria.where("id").in(ids));
        try {
            mongoTemplate.findAllAndRemove(query, entityClass);
        } finally {
            logger.debug("batch delete,query={}, entityClass={}, elapsedTime={}", query, entityClass.getName(), watch.elapsedTime());
        }
    }

    public long count(Query query, Class<?> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.count(query, entityClass);
        } finally {
            logger.debug("count, query={}, entityClass={}, elapsedTime={}", query, entityClass.getName(), watch.elapsedTime());
        }
    }

    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> inputTypeClass, Class<O> outputTypeClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.aggregate(aggregation, inputTypeClass, outputTypeClass);
        } finally {
            logger.debug("aggregate, aggregation={}, inputTypeClass={}, outputType={}, elapsedTime={}", aggregation, inputTypeClass, outputTypeClass.getName(),
                         watch.elapsedTime());
        }
    }

    public List distinct(String key, Class<?> entityClass) {
        return distinct(key, new Query(), entityClass);
    }

    public List distinct(String key, Query query, Class<?> entityClass) {
        StopWatch watch = new StopWatch();
        try {
            return mongoTemplate.getCollection(mongoTemplate.getCollectionName(entityClass)).distinct(key, query.getQueryObject());
        } finally {
            logger.debug("distinct,  key={}, query={}, entityClass={}, elapsedTime={} ", key, query, entityClass.getName(), watch.elapsedTime());
        }
    }

    public BulkWriteResult bulkWrite(Class<?> entityClass, BulkWriteInvoker bulkWriteInvoker) {
        StopWatch watch = new StopWatch();
        BulkWriteResult bulkWriteResult = null;
        try {
            bulkWriteResult = bulkWriteInvoker.doWrite(mongoTemplate.getCollection(mongoTemplate.getCollectionName(entityClass)));
            return bulkWriteResult;
        } finally {
            logger.debug("bulkWrite,  entityClass={}, bulkWriteResult={}, elapsedTime={} ", entityClass.getName(), bulkWriteResult, watch.elapsedTime());
        }
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}