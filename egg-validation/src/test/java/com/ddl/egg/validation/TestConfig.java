package com.ddl.egg.validation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mark on 2017/3/29.
 */
@Configuration
@EnableDubboValidate
@ComponentScan(basePackages = "com.ddl.egg.validation")
public class TestConfig {
}
