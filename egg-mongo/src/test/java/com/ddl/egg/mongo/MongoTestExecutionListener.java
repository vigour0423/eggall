package com.ddl.egg.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Created by mark.huang on 2016-06-16.
 */
public class MongoTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        MongoTemplate mongoTemplate = (MongoTemplate) testContext.getApplicationContext().getBean("mongoTemplate");
        if (!mongoTemplate.getDb().getName().contains("-test-")) {
            throw new RuntimeException("must run test case on test db!");
        }
    }
}

