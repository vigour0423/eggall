package com.ddl.egg.common.mybatis.extensions;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义MyBatis枚举类型处理器，可将枚举自动转换为Integer类型和String类型
 *
 * Created by LuoPeng on 2016-06-17.
 */
public class ValuedEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private Class<E> type;
    private Map<Object, E> map = new HashMap<>();

    public ValuedEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        E[] enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
        for (E e : enums) {
            ValuedEnum valuedEnum = (ValuedEnum) e;
            map.put(valuedEnum.getValue(), e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ValuedEnum valuedEnum = (ValuedEnum) parameter;
        Object value = valuedEnum.getValue();
        if(value instanceof Integer){
            ps.setInt(i, ((Integer) value).intValue());
        }else{
            ps.setString(i, value.toString());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object value = rs.getObject(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return getValuedEnum(value);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object value = rs.getObject(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return getValuedEnum(value);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object value = cs.getObject(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return getValuedEnum(value);
        }
    }

    private E getValuedEnum(Object value) {
        try {
            return map.get(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Cannot convert " + value + " to " + type.getSimpleName() + " by value.", ex);
        }
    }
}