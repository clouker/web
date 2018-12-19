package org.clc.common.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //        @Pointcut("execution( * org.clc.web.controller..*.*(..))")
    // execution()语法:execution (* com.xx.xx.impl..*.*(..))
    //@Pointcut("within(com.test.spring.aop.pointcutexp..*)")
    //@Pointcut("this(com.test.spring.aop.pointcutexp.Intf)")
    //@Pointcut("target(com.test.spring.aop.pointcutexp.Intf)")
    //@Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
    @Pointcut("@annotation(org.clc.common.annotation.Action)")
//    @Pointcut("args(String)")
    public void logPointCut() {
    }
}
