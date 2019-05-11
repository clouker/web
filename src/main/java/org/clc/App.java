package org.clc;

import org.clc.common.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App{

    // 登陆拦截器
    private final LoginInterceptor loginInterceptor;

    @Autowired
    public App(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * 注册拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
//        log.info("InterceptorRegistry : Start...");
//        log.info("          ---loginInterceptor : Open...");
        registry.addInterceptor(loginInterceptor);
//        log.info("InterceptorRegistry : Success...");
    }
}
