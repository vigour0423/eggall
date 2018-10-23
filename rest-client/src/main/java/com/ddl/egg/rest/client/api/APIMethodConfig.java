package com.ddl.egg.rest.client.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface APIMethodConfig {

    int timeout() default 0;

    String charsetEncoding() default "";
}
