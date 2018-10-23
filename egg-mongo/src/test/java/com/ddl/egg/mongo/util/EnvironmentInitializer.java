package com.ddl.egg.mongo.util;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by mark.huang on 2016-06-12.
 */
public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        PropertyLoader propertyLoader = new PropertyLoader();
        try {
            //* to load main resources,but would load some unexpected properties file like jdk,idea's by mark
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