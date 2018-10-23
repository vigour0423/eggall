package com.ddl.egg.validation.custom.v2.validator;

import com.ddl.egg.validation.custom.v2.annotation.OmitNullSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Collections;

/**
 * @author mark.huang
 */
public class OmitNullSizeValidator implements ConstraintValidator<OmitNullSize, Collection<?>> {

    private int min;
    private int max;

    @Override
    public void initialize(OmitNullSize omitNullSize) {
        this.min = omitNullSize.min();
        this.max = omitNullSize.max();
        this.validateParameters();
    }

    @Override
    public boolean isValid(Collection<?> objects, ConstraintValidatorContext constraintValidatorContext) {
        if (objects == null) {
            return true;
        } else {
            objects.removeAll(Collections.singleton(null));
            int length = objects.size();
            return length >= this.min && length <= this.max;
        }
    }

    private void validateParameters() {
        if (this.min < 0) {
            throw new RuntimeException("min can not be negative.");
        } else if (this.max < 0) {
            throw new RuntimeException("max can not be negative.");
        } else if (this.max < this.min) {
            throw new RuntimeException("max can not be less than min.");
        }
    }
}
