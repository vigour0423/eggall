package com.ddl.egg.rest.client.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface API {

    String serverName() default "";

    String serverUrl() default "";

    String charsetEncoding() default "utf-8";
}
