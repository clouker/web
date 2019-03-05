package org.clc.utils;

import org.clc.pojo.Pojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class Request {

    private static Logger log = LoggerFactory.getLogger(Request.class);

    private static HttpServletRequest request = null;

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return request == null ? ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest() : request;
    }

    public Request() {
        request = Request.getRequest();
        System.out.println("------------服务器信息------------");
        System.out.println("------获取请求使用的协议名【request.getScheme()】:" + request.getScheme() + "------");
        System.out.println("------获取请求使用的具体协议版本【request.getProtocol()】:" + request.getProtocol() + "------");
        System.out.println("------获取请求URL上的主机名【request.getServerName()】:" + request.getServerName() + "------");
        System.out.println("------获取请求URL上的端口号【request.getServerPort()】:" + request.getServerPort() + "------");
        System.out.println("------获取最终接收请求的主机地址【request.getLocalAddr()】:" + request.getLocalAddr() + "------");
        System.out.println("------获取最终接收请求的主机名【request.getLocalName()】:" + request.getLocalName() + "------");
        System.out.println("------获取最终接收请求的端口【request.getLocalPort()】:" + request.getLocalPort() + "------");
        System.out.println("------获取请求的方法【request.getMethod()】:" + request.getMethod() + "------");
        System.out.println("------获取请求URL从端口到请求参数中间的部分【request.getRequestURI()】:" + request.getRequestURI() + "------");
        System.out.println("------获取请求URL【request.getRequestURL()】:" + request.getRequestURL() + "------");
        System.out.println("------获取请求URL中访问servlet的那部分路径【request.getServletPath()】:" + request.getServletPath() + "------");
        System.out.println("------获取给定虚拟路径在服务端的真实路径，从servlet3.0开始【request.getServletContext()】:" + request.getServletContext() + "------");
        System.out.println("------获取给定虚拟路径在服务端的真实路径，从servlet2.3开始【request.getSession().getServletContext().getRealPath('/')】:" + request.getSession().getServletContext().getRealPath("/") + "------");
        System.out.println("------客户端信息------");
        System.out.println("------获取发送请求的客户端地址【request.getRemoteAddr()】:" + request.getRemoteAddr() + "------");
        System.out.println("------获取发送请求的客户端主机名【request.getRemoteHost()】:" + request.getRemoteHost() + "------");
        System.out.println("------获取发送请求的客户端端口【request.getRemotePort()】:" + request.getRemotePort() + "------");

    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 设置session属性
     */
    public static void setSessionAttr(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 设置登陆用户信息
     */
    public static void setUserSession(Pojo map) {
        getSession().setAttribute("userInfo", map);
        getSession().setAttribute("userId", map.get("id"));
    }

    /**
     * 获取登陆用户
     */
    public static Pojo getUserSession() {
        return (Pojo) getSession().getAttribute("userInfo");
    }

    /**
     * 获取登陆用户ID
     */
    public static String getUserIdSession() {
        Object userId = getSession().getAttribute("userId");
        return userId == null ? null : userId.toString();
    }

    /**
     * 移除登陆用户信息
     */
    public static void removeUserSession() {
        getSession().removeAttribute("userInfo");
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     * 取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 用户真实IP为： 192.168.1.110
     */
    public static String getIpAddress() {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null)
            log.info("X-Forwarded-For=" + ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (ip != null)
                    log.info("Proxy-Client-IP=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (ip != null)
                    log.info("WL-Proxy-Client-IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (ip != null)
                    log.info("HTTP_CLIENT_IP=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (ip != null)
                    log.info("HTTP_X_FORWARDED_FOR=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (ip != null)
                    log.info("getRemoteAddr=" + ip);
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String ip1 : ips) {
                if (!("unknown".equalsIgnoreCase(ip1))) {
                    ip = ip1;
                    break;
                }
            }
        }
        return ip;
    }
}
