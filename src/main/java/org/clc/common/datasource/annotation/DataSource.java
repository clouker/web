package org.clc.common.datasource.annotation;

import org.clc.common.datasource.config.DynamicDataSource;
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



    @AliasFor("dataSource")
    String value() default DynamicDataSource.dataSourceDefault;

    @AliasFor("value")
    String dataSource() default DynamicDataSource.dataSourceDefault;

}
