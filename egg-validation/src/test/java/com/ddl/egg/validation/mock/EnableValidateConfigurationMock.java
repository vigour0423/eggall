package com.ddl.egg.validation.mock;

import com.alibaba.dubbo.common.Constants;
import com.ddl.egg.validation.interceptor.MethodValidationInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by mark on 2017/3/29.
 */
@Configuration
@EnableAspectJAutoProxy
public class EnableValidateConfigurationMock {

    private static final String SINGLE_EXPRESSION = "execution(public * %PACKAGE%..*.*(..))";


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
        Pointcut annoPointCut = new AnnotationMatchingPointcut(MockService.class, true);

        Pointcut aspectjPointcut = new AspectJExpressionPointcut();
        String expression = Arrays.stream(Constants.COMMA_SPLIT_PATTERN.split("com.hk.egg.validation.mock.service.validate")).map(path -> SINGLE_EXPRESSION.replaceAll("%PACKAGE%", path)).collect(Collectors.joining(" OR "));
        ((AspectJExpressionPointcut) aspectjPointcut).setExpression(expression);

        composablePointcut.intersection(annoPointCut).intersection(aspectjPointcut);
        return composablePointcut;

    }

}
