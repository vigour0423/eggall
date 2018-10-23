package com.ddl.egg.common.mybatis.extensions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *  自定义枚举接口类
 *  实现类Object类型支持Integer类型和String类型
 *  通过ValuedEnumTypeHandler处理可自动判断转换
 *
 * Created by LuoPeng on 2016-06-17.
 */
@JsonSerialize(using = ValuedEnumObjectSerializer.class)
public interface ValuedEnum {
    public Object getValue();
    public String getText();
}
