package org.clc.common.datasource.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.clc.common.datasource.annotation.DataSource;
import org.clc.common.datasource.config.DynamicContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 多数据源，切面处理类
 */
@Aspect
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(org.clc.common.datasource.annotation.DataSource) || @within(org.clc.common.datasource.annotation.DataSource)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dataSource;
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        DataSource methodDataSource = AnnotationUtils.findAnnotation(methodSignature.getMethod(), DataSource.class);
        if (methodDataSource != null) {
            dataSource = methodDataSource.dataSource();
        } else {
            Class targetClass = point.getTarget().getClass();
            //类不存在@DataSource，直接返回
            if (targetClass == null)
                return point.proceed();
            DataSource classDataSource = AnnotationUtils.findAnnotation(targetClass, DataSource.class);
            if (classDataSource == null)
                return point.proceed();
            dataSource = classDataSource.dataSource();
        }
        try {
            DynamicContextHolder.push(dataSource);
            logger.info("The datasource is {}.", dataSource);
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
            logger.info("clean datasource {}.", dataSource);
        }
    }
}