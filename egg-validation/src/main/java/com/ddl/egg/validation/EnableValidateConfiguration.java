package com.ddl.egg.validation;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.ddl.egg.validation.interceptor.MethodValidationInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.ddl.egg.BaseConstants.PROFILE_TEST;


/**
 * Created by mark on 2017/3/28.
 */
@Configuration
@EnableAspectJAutoProxy
@Profile("!" + PROFILE_TEST)
public class EnableValidateConfiguration implements ImportAware {

    private static final String SINGLE_EXPRESSION = "execution(public * %PACKAGE%..*.*(..))";

    private String dubboServicePackage;

    @Autowired
    private ApplicationContext applicationContext;

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableDubboValidate.class.getName(), false));
        Assert.notNull(annotationAttributes, "@EnableServiceValidate is not present on importing class " + importMetadata.getClassName());

        AnnotationBean annotationBean = findAnnotationBean();
        //todo(mark) check provider or cosumer?
        this.dubboServicePackage = annotationBean.getPackage();
        Assert.hasText(dubboServicePackage, "can not find dubbo annotationBean service package config, maybe empty.");
    }

    private AnnotationBean findAnnotationBean() {
        try {
            return applicationContext.getBean(AnnotationBean.class);
        } catch (Exception e) {
            throw new RuntimeException("can not find dubbo annotationBean config, maybe you use dubbo in xml config(not supported).");
        }
    }

    @Bean
    public MethodValidationInterceptor methodValidationInterceptor() {
        return new MethodValidationInterceptor();
    }

    @Bean
    public Advisor methodValidateAdvisor() {
        return new DefaultPointcutAdvisor(pointcut(), methodValidationInterceptor());
    }

    private Pointcut pointcut() {
        ComposablePointcut composablePointcut = new ComposablePointcut();
        Pointcut annoPointCut = new AnnotationMatchingPointcut(Service.class, true);

        Pointcut aspectjPointcut = new AspectJExpressionPointcut();
        String expression = Arrays.stream(Constants.COMMA_SPLIT_PATTERN.split(dubboServicePackage)).map(path -> SINGLE_EXPRESSION.replaceAll("%PACKAGE%", path)).collect(Collectors.joining(" OR "));
        ((AspectJExpressionPointcut) aspectjPointcut).setExpression(expression);

        composablePointcut.intersection(annoPointCut).intersection(aspectjPointcut);
        return composablePointcut;

    }

}
