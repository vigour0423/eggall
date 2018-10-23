package com.ddl.egg.validation.custom.v2.validator;

import com.ddl.egg.validation.custom.v2.annotation.CollectionVerify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author mark.huang
 */
public class CollectionVerifyValidator implements ConstraintValidator<CollectionVerify, Collection<?>> {
    private boolean nullable;

    @Override
    public void initialize(CollectionVerify collectionVerify) {
        nullable = collectionVerify.nullable();
    }

    @Override
    public boolean isValid(Collection<?> objects, ConstraintValidatorContext constraintValidatorContext) {
        return nullable || (objects != null);
    }
}
