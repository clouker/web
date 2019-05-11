package org.clc.common.datasource.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 多数据源注解: 可作用于类、接口、方法
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSource {

    String dataSourceDefault = "dataSourceDefault";
    String dataSourceTest = "dataSourceTest";

    @AliasFor("dataSource")
    String value() default dataSourceDefault;

    @AliasFor("value")
    String dataSource() default dataSourceDefault;

}
