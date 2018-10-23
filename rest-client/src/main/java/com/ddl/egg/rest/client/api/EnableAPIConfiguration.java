package com.ddl.egg.rest.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public class EnableAPIConfiguration implements ImportBeanDefinitionRegistrar {
    private final Logger logger = LoggerFactory.getLogger(EnableAPIConfiguration.class);

    public void enableAPI(DefaultListableBeanFactory defaultListableBeanFactory) {
        logger.info("begin enable api");

        EnableAPISettings settings = defaultListableBeanFactory.getBean(EnableAPISettings.class);
        Assert.notNull(settings, "EnableAPISettings bean does not exist");
        Assert.notNull(settings.getPackageNameList(), "package name is required");

        for (String packageName : settings.getPackageNameList()) {
            EnableAPIService enableAPIService = new EnableAPIService(settings, packageName, defaultListableBeanFactory);
            enableAPIService.autoProxy();
        }

        logger.info("finished enable api");
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes enableAPI = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableAPI.class.getName(), false));
        Assert.notNull(enableAPI, "@EnableAPI is not present on importing class " + annotationMetadata.getClassName());

        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
        enableAPI(defaultListableBeanFactory);
    }
}
