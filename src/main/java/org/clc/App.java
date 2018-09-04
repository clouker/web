package org.clc;

import org.clc.common.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableCaching
//@EnableScheduling
@SpringBootApplication
public class App implements WebMvcConfigurer {

    @Autowired // 登陆拦截器
    private LoginInterceptor loginInterceptor;

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
