package com.ddl.egg.common.disconf;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean;
import com.ddl.egg.common.spring.PropertySourcesEnvPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by mark.huang on 2016-06-07.
 */
public abstract class DefaultDisConfConfig {

    public static String basePackage() {
        throw new RuntimeException("this method must be override.");
    }

    public abstract List<String> fileNames();

    @Bean(destroyMethod = "destroy")
    public static DisconfMgrBean disconfMgrBean() {
        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
        disconfMgrBean.setScanPackage(basePackage());
        return disconfMgrBean;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DisconfMgrBeanSecond disconfMgrBeanSecond() {
        return new DisconfMgrBeanSecond();
    }

    private ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean() throws IOException {
        ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean = new ReloadablePropertiesFactoryBean();
        reloadablePropertiesFactoryBean.setLocations(fileNames());
        reloadablePropertiesFactoryBean.afterPropertiesSet();
        return reloadablePropertiesFactoryBean;
    }

    @Bean
    public PropertySourcesEnvPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
        PropertySourcesEnvPlaceholderConfigurer propertySourcesEnvPlaceholderConfigurer = new PropertySourcesEnvPlaceholderConfigurer();
        propertySourcesEnvPlaceholderConfigurer.setIgnoreResourceNotFound(false);
        propertySourcesEnvPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(false);
        propertySourcesEnvPlaceholderConfigurer.setPropertiesArray(new Properties[]{reloadablePropertiesFactoryBean().getObject()});
        propertySourcesEnvPlaceholderConfigurer.setLocalOverride(true);
        return propertySourcesEnvPlaceholderConfigurer;
    }

}
