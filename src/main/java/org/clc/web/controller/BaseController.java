package org.clc.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.clc.common.Page;
import org.clc.kernel.pojo.Pojo;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class BaseController {

    //系统路径符号
    protected static final String PATH_FLAG = File.separator;
    //日志记录
    protected static Logger logger = log;

    /**
     * 获取请求实体
     *
     * @param table
     * @return
     */

    protected Pojo getPojo(String table) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Map<String, String[]> map = request.getParameterMap();
        Pojo pojo = new Pojo(table);
        map.forEach((k, v) -> {
            StringBuffer sb = new StringBuffer();
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
        return pojo;
    }

    /**
     * 基本实体对象
     *
     * @param table
     * @return
     */
    protected Pojo pojo(String table) {
        return new Pojo(table);
    }

    /**
     * 基本实体对象
     *
     * @param table
     * @param cols
     * @return
     */
    protected Pojo pojo(String table, String cols) {
        Pojo pojo = new Pojo(table);
        pojo.setCols(cols);
        return pojo;
    }

    /**
     * 分页信息整合
     *
     * @param table 表名
     * @param start 页码
     * @param size  单页容量
     * @return
     */
    protected Page page(String table, int start, int size) {
        return new Page(table, start, size);
    }

    /**
     * 分页信息整合
     *
     * @param table 表名
     * @param cols  返回固定字段
     * @param start 页码
     * @param size  单页容量
     * @return
     */
    protected Page page(String table, String cols, int start, int size) {
        Page page = new Page(table, start, size);
        page.setCols(cols);
        return page;
    }
}
