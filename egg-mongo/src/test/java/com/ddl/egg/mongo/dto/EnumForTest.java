package com.ddl.egg.mongo.dto;

import com.ddl.egg.mongo.converter.OrdinalEnumBean;

/**
 * Created by mark.huang on 2016-08-03.
 */
public enum EnumForTest implements OrdinalEnumBean {
    A(1), B(3);

    final int a;

    EnumForTest(int a) {
        this.a = a;
    }

    public int value() {
        return a;
    }
}
