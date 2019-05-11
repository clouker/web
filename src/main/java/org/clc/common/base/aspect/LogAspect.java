package org.clc.common.base.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.clc.common.base.annotation.ActionLog;
import org.clc.utils.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // @Pointcut("execution( * org.clc.web.controller..*.*(..))")
    //@Pointcut("within(com.test.spring.aop.pointcutexp..*)")
    //@Pointcut("this(com.test.spring.aop.pointcutexp.Intf)")
    //@Pointcut("target(com.test.spring.aop.pointcutexp.Intf)")
    //@Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
//    @Pointcut("args(String)")
    @Pointcut("@annotation(org.clc.common.base.annotation.ActionLog)")
    public void logPointCut() {
    }

    /*
     * @Before  在切点方法之前执行
     * @After  在切点方法之后执行
     * @AfterReturning 切点方法返回后执行
     * @AfterThrowing 切点方法抛异常执行
     * @Around 属于环绕增强，能控制切点执行前，执行后，
     *      用这个注解后，程序抛异常，会影响@AfterThrowing这个注解
     */
    @Before(value = "logPointCut()")
    public void before(JoinPoint joinPoint) {
        logger.info("@Before通知执行");
        //获取目标方法参数信息
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args).forEach(arg -> {
            try {
                logger.info(arg + "");
            } catch (Exception e) {
                logger.error(arg + "");
            }
        });
        //aop代理对象
        Object aThis = joinPoint.getThis();
        //被代理对象
        Object target = joinPoint.getTarget();
        //获取连接点的方法签名对象
        Signature signature = joinPoint.getSignature();
        logger.info(signature.toLongString()); //public java.lang.String com.xhx.springboot.controller.HelloController.getName(java.lang.String)
        logger.info(signature.toShortString()); //HelloController.getName(..)
        logger.info(signature.toString()); //String com.xhx.springboot.controller.HelloController.getName(String)
        //获取方法名
        logger.info(signature.getName()); //getName
        //获取声明类型名
        logger.info(signature.getDeclaringTypeName()); //com.xhx.springboot.controller.HelloController
        //连接点类型
        String kind = joinPoint.getKind();
        logger.info(kind);//method-execution
        /* //返回连接点方法所在类文件中的位置  打印报异常
         *  SourceLocation sourceLocation = joinPoint.getSourceLocation();
         *  logger.info(sourceLocation.toString());
         *  logger.info(sourceLocation.getFileName());
         *  logger.info(sourceLocation.getLine() + "");
         *  logger.info(sourceLocation.getWithinType().toString());
         */
        ///返回连接点静态部分
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
        logger.info(staticPart.toLongString());  //execution(public java.lang.String com.xhx.springboot.controller.HelloController.getName(java.lang.String))
        HttpServletRequest request = Request.getRequest();
        logger.info("URL: {}", request.getRequestURL().toString());//http://127.0.0.1:8080/hello/getName
        logger.info(Request.getIpAddress()); //127.0.0.1
        logger.info("Method: {}.", request.getMethod());
        logger.info("before通知执行结束");
    }

    @Around("logPointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) {
        logger.info("@Around环绕通知：" + joinPoint.getSignature().toString());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解信息
        //
        ActionLog annotation = AnnotationUtils.findAnnotation(method, ActionLog.class);
        ActionLog actionLogInfo = method.getAnnotation(ActionLog.class);
        String name = actionLogInfo.name();
        String description = actionLogInfo.description();
        boolean save = actionLogInfo.save();

        Object proceed = null;
        try {
            proceed = joinPoint.proceed();//可以加参数
            logger.info(proceed.toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        logger.info("@Around环绕通知执行结束");
        return proceed;
    }

    /**
     * 后置返回
     * 如果第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning：限定了只有目标方法返回值与通知方法参数类型匹配时才能执行后置返回通知，否则不执行，
     * 参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(value = "logPointCut()", returning = "result")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object result) {
        logger.info("@后置返回通知的返回值：" + result);
    }

    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     */
    @AfterThrowing(value = "logPointCut()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        logger.info(joinPoint.getSignature().getName());
        if (exception instanceof NullPointerException) {
            logger.info("发生了空指针异常!!!!!");
        }
    }

    @After(value = "logPointCut()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        logger.info("后置通知执行了!");
    }
}
