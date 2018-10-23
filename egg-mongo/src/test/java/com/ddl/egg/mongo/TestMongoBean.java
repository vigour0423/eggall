package com.ddl.egg.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by mark.huang on 2016-06-16.
 */
public class TestMongoBean {

    MongoTemplate mongoTemplate;

    final void end() {
        mongoTemplate.getDb().dropDatabase();
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
