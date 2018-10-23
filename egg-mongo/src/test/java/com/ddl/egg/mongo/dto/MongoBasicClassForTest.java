package com.ddl.egg.mongo.dto;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by mark.huang on 2016-08-03.
 */
public class MongoBasicClassForTest {

    private String id;
    @Field("integer")
    private Integer integer;
    @Field("class")
    private String testClass;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }
}
