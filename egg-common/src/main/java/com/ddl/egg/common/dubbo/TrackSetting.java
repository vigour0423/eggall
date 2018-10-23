package com.ddl.egg.common.dubbo;

import com.ddl.egg.log.monitor.Track;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by mark.huang on 2016-07-25.
 * 用来跟踪耗时大的处理
 */
public class TrackSetting implements BeanPostProcessor {

    public static final String PO = "-";
    private final Map<String, Integer> trackingMap = new HashMap<>();
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");


    private String dubboServicePackage;

    private boolean logRequestParam;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (isMatchPackage(bean)) {
            final Class interfaceImpl = bean.getClass();
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(interfaceImpl);
            String interfaceName = getInterfaceName(interfaceImpl);
            Arrays.stream(methods).forEach(method -> {
                Track annotation = AnnotationUtils.getAnnotation(method, Track.class);
                if (annotation != null && !StringUtils.isBlank(interfaceName)) {
                    trackingMap.put(interfaceName + PO + method.getName(), annotation.warningThresholdInMs());
                }
            });
        }
        return bean;
    }

    private String getInterfaceName(Class interfaceImpl) {
        Class<?>[] interfaces = interfaceImpl.getInterfaces();
        if (!ArrayUtils.isEmpty(interfaces)) {
            return interfaces[0].getName();
        }
        return null;
    }

    private boolean isMatchPackage(Object bean) {
        if (StringUtils.isBlank(dubboServicePackage)) {
            throw new IllegalArgumentException("dubbo service scan package can not be empty.");
        }

        Class clazz = bean.getClass();
        if (isProxyBean(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }
        String[] annotationPackages = COMMA_SPLIT_PATTERN.split(dubboServicePackage);
        for (String pkg : annotationPackages) {
            if (clazz.getName().startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void setDubboServicePackage(String dubboServicePackage) {
        this.dubboServicePackage = dubboServicePackage;
    }

    private boolean isProxyBean(Object bean) {
        return AopUtils.isAopProxy(bean);
    }

    public Integer get(String action) {
        return trackingMap.get(action);
    }

    public static String getPO() {
        return PO;
    }

    public Map<String, Integer> getTrackingMap() {
        return trackingMap;
    }

    public boolean isLogRequestParam() {
        return logRequestParam;
    }

    public void setLogRequestParam(boolean logRequestParam) {
        this.logRequestParam = logRequestParam;
    }
}
