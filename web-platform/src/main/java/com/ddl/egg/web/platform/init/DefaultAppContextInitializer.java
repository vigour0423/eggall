package com.ddl.egg.web.platform.init;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.IOException;
import java.util.Properties;


public class DefaultAppContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

    public void initialize(ConfigurableWebApplicationContext context) {
        PropertyLoader propertyLoader = new PropertyLoader();
        try {
            propertyLoader.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:*.properties"));

            context.getEnvironment().setIgnoreUnresolvableNestedPlaceholders(true);
            context.getEnvironment().getPropertySources().addLast(new PropertiesPropertySource("properties", propertyLoader.loadProperties()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class PropertyLoader extends PropertySourcesPlaceholderConfigurer {
        Properties loadProperties() throws IOException {
            return mergeProperties();
        }
    }
}