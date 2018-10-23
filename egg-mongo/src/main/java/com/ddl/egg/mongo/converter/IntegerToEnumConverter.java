package com.ddl.egg.mongo.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author mark
 */
@ReadingConverter
public class IntegerToEnumConverter implements ConditionalGenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Integer.class, OrdinalEnumBean.class));
    }


    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) return null;

        for (OrdinalEnumBean enumObj : ((Class<? extends OrdinalEnumBean>) targetType.getType()).getEnumConstants()) {
            if (enumObj.value() == (Integer) source)
                return enumObj;
        }
        return null;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        //只允许枚举
        return OrdinalEnumBean.class.isAssignableFrom(targetType.getType()) && Enum.class.isAssignableFrom(targetType.getType());
    }
}