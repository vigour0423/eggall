package com.ddl.egg.mongo.dto;

import com.ddl.egg.mongo.converter.OrdinalEnumBean;

/**
 * Created by mark.huang on 2016-08-03.
 */
public class ClassForTest implements OrdinalEnumBean {

    final int a;

    public ClassForTest(int a) {
        this.a = a;
    }

    public int value() {
        return a;
    }
}
