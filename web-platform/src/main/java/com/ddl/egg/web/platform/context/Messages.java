package com.ddl.egg.web.platform.context;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;


public class Messages extends ResourceBundleMessageSource {
    public String getMessage(String key, Object... arguments) {
        return super.getMessage(key, arguments, key, Locale.getDefault());
    }
}
