package org.clc.utils;

import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

/**
 * 定义请求返回对象
 */
public class Response extends HashMap{
    private int statusCode;
    private String statusStr;
    private String content;
    private List<Cookie> cookies;
    private String contentType;
    /**
     * 通过id选择器获取元素字符串
     *
     * @param id
     * @return
     */
    public String getElementById(String id) {
        Document document = Jsoup.parse(this.content != null ? this.content : "");
        Element element = document.getElementById(id);
        return element != null ? element.toString() : "";
    }

    /**
     * 通过class选择器获取元素字符串
     *
     * @param className
     * @return
     */
    public String getElementsByClass(String className) {
        Document document = Jsoup.parse(this.content != null ? this.content : "");
        Elements element = document.getElementsByClass(className);
        return element != null ? element.toString() : "";
    }

    /**
     * 通过CSS选择器获取元素字符串
     *
     * @param query css选择器
     * @return
     */
    public String getElementsStr(String query) {
        return getElements(query).toString();
    }

    /**
     * 通过CSS选择器获取元素
     *
     * @param query css选择器
     * @return
     */
    public Elements getElements(String query) {
        Document document = Jsoup.parse(this.content != null ? this.content : "");
        Elements element = document.select(query);
        return element != null ? element : new Elements();
    }

    public Response(int statusCode, String statusStr, String content,String contentType,List<Cookie> cookies) {
        this.content = content;
        this.cookies = cookies;
        this.statusStr = statusStr;
        this.statusCode = statusCode;
        this.contentType = contentType;
    }
}