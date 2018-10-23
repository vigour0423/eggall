package com.ddl.egg.mongo.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author mark
 */
@WritingConverter
public class EnumToIntegerConverter implements ConditionalGenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(OrdinalEnumBean.class, Integer.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) return null;

        return ((OrdinalEnumBean) source).value();
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        //只允许枚举
        return OrdinalEnumBean.class.isAssignableFrom(sourceType.getType()) && Enum.class.isAssignableFrom(sourceType.getType());
    }
}