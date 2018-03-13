package org.clc.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"s","s"})
@PropertySource(value = {"属性文件位置"},ignoreResourceNotFound = true)
public class AppConfig {
    @Value("")
    private String a;
}
