package com.ddl.egg.validation.custom.v2.annotation;

import com.ddl.egg.validation.custom.v2.validator.CollectionVerifyValidator;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mark.huang
 */
@OmitNullSize
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CollectionVerifyValidator.class)
public @interface CollectionVerify {

    @OverridesAttribute(
            constraint = OmitNullSize.class,
            name = "message"
    ) String message() default "collection can not be null";

    boolean nullable();

    @OverridesAttribute(
            constraint = OmitNullSize.class,
            name = "min"
    ) int min();

    @OverridesAttribute(
            constraint = OmitNullSize.class,
            name = "max"
    ) int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
