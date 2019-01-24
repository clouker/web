package org.clc.common;

import io.swagger.annotations.ApiOperation;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@Configuration
@EnableSwagger2
@EnableScheduling
public class AppConfig {

    @Value("${server.ssl.key-store}")
    private String path;
    @Value("${server.ssl.key-store-password}")
    private String password;
    @Value("${server.port}")
    private int port;

    //*********************************** Swagger2 ***********************************//
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .apis(RequestHandlerSelectors.basePackage("org.clc.web.controller.*.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("网站API文档")
                //.description("restful 风格接口")
                //.termsOfServiceUrl("服务条款网址")
                .version("1.0")
                //.contact(new Contact("name", "url", "email"))
                .build();
    }

    /**
     * 通过构造工厂造1个jetty
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        JettyServletWebServerFactory jetty = new JettyServletWebServerFactory();
        customizeJetty(jetty);
        return jetty;
    }

    /**
     * 为jetty服务器开通http端口和https,并配置线程
     */
    private void customizeJetty(JettyServletWebServerFactory container) {
        container.addServerCustomizers((Server server) -> {
            //配置线程
            threadPool(server);
            // 添加HTTP配置
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(port);
            // 添加HTTPS配置
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath(path);
            sslContextFactory.setKeyStorePassword(password);

            HttpConfiguration config = new HttpConfiguration();
            config.setSecureScheme(HttpScheme.HTTPS.asString());
            config.addCustomizer(new SecureRequestCustomizer());

            ServerConnector sslConnector = new ServerConnector(
                    server,
                    new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                    new HttpConnectionFactory(config));
            sslConnector.setPort(port + 1);
            server.setConnectors(new Connector[]{connector, sslConnector});
        });
    }

    /**
     * jetty线程配置
     */
    private void threadPool(Server server) {
        // connections
        final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
        //默认最大线程连接数200
        threadPool.setMaxThreads(300);
        //默认最小线程连接数8
        threadPool.setMinThreads(15);
        //默认线程最大空闲时间60000ms
        threadPool.setIdleTimeout(60000);
    }
}
