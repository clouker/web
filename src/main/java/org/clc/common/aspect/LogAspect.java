package org.clc.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

//    @Pointcut("execution( * org.clc.web.controller..*.*(..))")
    // execution()语法:execution (* com.xx.xx.impl..*.*(..))
    //@Pointcut("within(com.test.spring.aop.pointcutexp..*)")
    //@Pointcut("this(com.test.spring.aop.pointcutexp.Intf)")
    //@Pointcut("target(com.test.spring.aop.pointcutexp.Intf)")
    //@Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
    //@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    //@Pointcut("args(String)")
//    public void logPointCut() {
//    }

//    @Around("execution( * org.clc.web.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob 为方法的返回值
        logger.info("耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }

    @After("@annotation(org.clc.common.annotation.Log)")
    public void doAfter(JoinPoint point) {
        logger.info("After... ");
    }

//    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
//    public void doAfterReturning(Object ret) {
//        logger.info("AfterReturning... ");
//        logger.info("返回值类型 -> {}", ret.getClass());
//        logger.info("返回值 : " + ret);
//    }
}
