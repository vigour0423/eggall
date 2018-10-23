package com.ddl.egg.mongo;

import com.ddl.egg.mongo.util.EnvironmentInitializer;
import com.ddl.egg.mongo.access.MongoAccess;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(classes = {TestMongoConfig.class}, initializers = EnvironmentInitializer.class)})

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoTestExecutionListener.class})
public abstract class SpringTest {

    @Autowired
    protected MongoAccess mongoAccess;

    protected void cleanMongo(Class<?>... classes) {
        Query query = new Query();
        Arrays.stream(classes).forEach(cls -> mongoAccess.delete(query, cls));
    }
}
