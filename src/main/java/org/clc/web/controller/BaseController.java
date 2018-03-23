package org.clc.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.clc.common.Page;
import org.clc.kernel.pojo.Pojo;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class BaseController {

    //系统路径符号
    protected static final String PATH_FLAG = File.separator;
    //日志记录
    protected static Logger logger = log;

    /**
     * 获取请求实体
     *
     * @param entity
     * @param <T>
     * @return
     */

    protected <T> T getPojo(Class<T> entity) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Map<String, String[]> map = request.getParameterMap();
        T t = null;
        try {
            t = entity.newInstance();
        } catch (Exception e) {
            logger.error("解析失败。。。");
            e.printStackTrace();
        }
        Pojo pojo = (Pojo) t;
        String cls = t.getClass().getSimpleName().toUpperCase();
        map.forEach((k, v) -> {
            StringBuffer sb = new StringBuffer();
            if (k.toUpperCase().startsWith(cls)) //去除实体类前缀
                k = k.substring(cls.length() + 1);
            if (v.length == 1)
                sb.append(v[0]);
            else {// 单key多val时，v以，相连
                for (String s : v)
                    sb.append(s + ",");
            }
            pojo.put(k, sb.toString());
        });
        // 判断是否包含上传文件
        if (request.getContentType() != null && request.getContentType().toString().startsWith("multipart/form-data")) {
            Collection<Part> parts;
            try {
                parts = request.getParts();
                parts.forEach(item -> {
                    if (item.getSize() != -1)
                        pojo.put("file", item);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    protected Pojo pojo(String table) {
        return pojo(table, "*");
    }

    protected Pojo pojo(String table, String cols) {
        return new Pojo(table, cols);
    }

    protected Page page(String table, int start, int size) {
        return page(table, " * ", start, size);
    }

    /**
     * 分页信息整合
     *
     * @param table 表名
     * @param cols  返回固定字段
     * @param start 页码
     * @param size 单页容量
     * @return
     */
    protected Page page(String table, String cols, int start, int size) {
        Page page = new Page(table, start, size);
        page.setCols(cols);
        return page;
    }


}
