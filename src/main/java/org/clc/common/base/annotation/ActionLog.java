package org.clc.common.base.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Web请求Log标识
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionLog {
    /*
        name value 互为别名：设置一个，另个就会有同样值
        使用注意：
            .其为spring注解，必须使用spring的AnnotationUtils类处理才生效（java默认处理注解，不生效）
            .互为别名的属性必须默认值一样，同时设置两个，必须值一样（没必要设置两个，这样注解就没意义了）
     */
    @AliasFor("name")
    String value() default "";

    @AliasFor(attribute = "value")
    String name() default "";

    String description() default "";

    boolean save() default true; // save to database
}
