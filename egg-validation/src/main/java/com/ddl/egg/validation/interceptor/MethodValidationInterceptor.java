package com.ddl.egg.validation.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Created by mark on 2017/3/28.
 */
public class MethodValidationInterceptor extends org.springframework.validation.beanvalidation.MethodValidationInterceptor {

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return super.invoke(invocation);
        } catch (ConstraintViolationException e) {
            //todo(mark) 目前不考虑抛出全部的异常校验
            ConstraintViolation constraintViolation = e.getConstraintViolations().iterator().next();
            throw new IllegalArgumentException(constraintViolation.getPropertyPath() + "->" + constraintViolation.getMessage());
        }
    }

}