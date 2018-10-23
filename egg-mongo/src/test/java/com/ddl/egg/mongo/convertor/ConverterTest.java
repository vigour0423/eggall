package com.ddl.egg.mongo.convertor;

import com.ddl.egg.mongo.SpringTest;
import com.ddl.egg.mongo.dto.ClassForTest;
import com.ddl.egg.mongo.dto.EnumForTest;
import com.ddl.egg.mongo.dto.MongoBasicClassForTest;
import com.ddl.egg.mongo.dto.MongoClassForTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by mark.huang on 2016-08-03.
 */
public class ConverterTest extends SpringTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testConvert() {
        MongoClassForTest testMongoClass = new MongoClassForTest();
        testMongoClass.setTestEnum(EnumForTest.B);
        mongoAccess.save(testMongoClass);


        List<MongoBasicClassForTest> testMongoClasses = mongoTemplate.findAll(MongoBasicClassForTest.class, "testMongoClass");
        Assert.assertEquals(1, testMongoClasses.size());
        Assert.assertTrue(3 == testMongoClasses.get(0).getInteger());

        List<MongoClassForTest> mongoClassForTests = mongoTemplate.findAll(MongoClassForTest.class);
        Assert.assertEquals(1, mongoClassForTests.size());
        Assert.assertEquals(EnumForTest.B.name(), mongoClassForTests.get(0).getTestEnum().name());
    }

    @Test(expected = ConverterNotFoundException.class)
    public void testConvertWithException() {
        MongoClassForTest testMongoClass = new MongoClassForTest();
        testMongoClass.setTestEnum(EnumForTest.B);

        ClassForTest testClass = new ClassForTest(2);
        testMongoClass.setTestClass(testClass);
        mongoAccess.save(testMongoClass);
    }
}
