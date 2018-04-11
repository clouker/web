package org.clc.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if (requestURI.contains("/css/")
                || requestURI.contains("/plugin/")
                || requestURI.contains("/js/")
                || requestURI.contains("/fonts/")
                || requestURI.contains("/images/"))
            return true;
        else
            return true;
    }
}
