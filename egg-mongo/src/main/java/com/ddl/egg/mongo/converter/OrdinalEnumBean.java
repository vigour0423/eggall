package com.ddl.egg.mongo.converter;

import java.util.Arrays;

/**
 * @author mark
 */
public interface OrdinalEnumBean {
    //用来处理mongo中不规则的数字枚举.
    int value();

    default OrdinalEnumBean enumValue(Integer value) {
        if (value == null) return null;
        return Arrays.stream(this.getClass().getEnumConstants()).filter(enumObj -> enumObj.value() == value).findFirst().get();
    }

}
