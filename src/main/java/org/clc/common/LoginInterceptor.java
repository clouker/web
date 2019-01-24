package org.clc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        log.info(requestURI);
        if (requestURI.contains("/css/") || requestURI.contains("/plugin/")
                || requestURI.contains("/js/") || requestURI.contains("/fonts/")
                || requestURI.contains("/images/"))
            return true;
        else
            return true;
    }
}
