package org.clc.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class AppConfig implements WebMvcConfigurer {

//    @Autowired // 登陆拦截器
//    private LoginInterceptor loginInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry
     */
//    public void addInterceptors(InterceptorRegistry registry) {
//        log.info("InterceptorRegistry : Start...");
//        log.info("          ---loginInterceptor : Open...");
//        registry.addInterceptor(loginInterceptor);
//        log.info("InterceptorRegistry : Success...");
//    }
}
