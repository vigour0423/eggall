package com.ddl.egg.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by mark.huang on 2016-08-03.
 */
@Document(collection = "testMongoClass")
public class MongoClassForTest {

    @Id
    private String id;
    @Field("integer")
    private EnumForTest testEnum;
    @Field("class")
    private ClassForTest testClass;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnumForTest getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(EnumForTest testEnum) {
        this.testEnum = testEnum;
    }

    public ClassForTest getTestClass() {
        return testClass;
    }

    public void setTestClass(ClassForTest testClass) {
        this.testClass = testClass;
    }
}
