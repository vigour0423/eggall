package com.ddl.egg.validation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by mark on 2017/3/28.
 */
public class EnableValidateSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{EnableValidateConfiguration.class.getName()};
    }
}