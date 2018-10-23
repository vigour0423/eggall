package com.ddl.egg.common.mybatis.extensions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * ValuedEnum序列化转换器：转换为对象
 */
public class ValuedEnumObjectSerializer extends JsonSerializer<ValuedEnum> {

    @Override
    public void serialize(ValuedEnum valuedEnum, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Object value = valuedEnum.getValue();
        String text = valuedEnum.getText();
        jgen.writeStartObject();
        if (value instanceof Integer) {
            jgen.writeNumberField("value",(Integer) value);
        } else {
            jgen.writeStringField("value",value.toString());
        }
        jgen.writeStringField("text",text);
        jgen.writeEndObject();
    }
}