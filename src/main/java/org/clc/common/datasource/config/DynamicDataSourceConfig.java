package org.clc.common.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置多数据源
 */
@Configuration
public class DynamicDataSourceConfig {

    /**
     * 默认数据源
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(name = DynamicDataSource.dataSourceDefault)
    public DataSource dataSourceDefault() {
        return DataSourceBuilder.create().build();
    }

    /**
     * test数据源
     */
    @ConfigurationProperties(prefix = "spring.datasource.test")
    @Bean(name = DynamicDataSource.dataSourceTest)
    public DataSource dataSourceTest() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * 将数据库实例写入到targetDataSources属性中，并且使用defaultTargetDataSource属性设置默认数据源。
     *
     * @ Primary 注解用于标识默认使用的 DataSource Bean，并注入到SqlSessionFactory的dataSource属性中去。
     */
    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSourceDefault());
        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap();
        dsMap.put("dataSourceDefault", dataSourceDefault());
        dsMap.put("dataSourceTest", dataSourceTest());
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

    /**
     * 配置@Transactional注解事物
     * 使用dynamicDataSource作为transactionManager的入参来构造DataSourceTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}