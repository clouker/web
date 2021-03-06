package org.clc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clc.pojo.Page;
import org.clc.pojo.Pojo;
import org.clc.utils.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.util.Collection;
import java.util.Map;

public class BaseController {

    //日志记录
    protected static Logger log = LoggerFactory.getLogger(BaseController.class);

    /* jackson 		JSON<==>POJO 操作类为ObjectMapper
     * Class2JSON
     * s@writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
     * @writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
     * @writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
     * @writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串
     *   pojo对象转JSON----mapper.writeValueAsString(pojo)  ======>    pojo = {"name":"小民","age":20}
     *   list集合转JSON---mapper.writeValueAsString(list)   ======>    list = [{"name":"小民","age":20}]
     * JSON2Class
     * @ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化
     *   mapper.readValue(json, Pojo.class)      ======>     json = "{'name':'小民','age':20,}";
     */

    /**
     * 获取请求实体
     */
    protected Pojo params() throws Exception {
        Pojo pojo = new Pojo();
        HttpServletRequest request = Request.getRequest();
        switch (request.getMethod()) {
            case "GET":
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (parameterMap.size() > 0)
                    pojo.putAll(parameterMap);
                break;
            case "POST":
            default:
                String contentType = request.getContentType();
                if (contentType != null) {
                    if (contentType.startsWith("application/json")) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        BufferedReader reader = request.getReader();
                        while ((line = reader.readLine()) != null)
                            sb.append(line);
                        ObjectMapper mapper = new ObjectMapper();
                        pojo = mapper.readValue(sb.toString(), Pojo.class);
                    } else {
                        Map<String, String[]> map = request.getParameterMap();
                        Pojo $pojo = new Pojo();
                        map.forEach((k, v) -> {
                            StringBuilder sb = new StringBuilder();
                            if (v.length == 1)
                                sb.append(v[0]);
                            else// 单key多val时，v以，相连
                                for (String s : v)
                                    sb.append(s).append(",");
                            $pojo.put(k, sb.toString());
                        });
                        pojo = $pojo;
                        // 判断是否包含上传文件
                        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
                            Collection<Part> parts = request.getParts();
                            Pojo upFiles = new Pojo();
                            parts.forEach(item -> {
                                if (item.getSubmittedFileName() != null) {
                                    try {
                                        upFiles.put(item.getName(), item.getInputStream());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            if (upFiles.size() > 0) {
                                if (pojo.size() > 0)
                                    pojo.putAll(upFiles);
                                else
                                    pojo = upFiles;
                            }
                        }
                    }
                }
        }

        return pojo;
    }

    /**
     * 基本实体对象
     */
    protected Pojo pojo(String table) {
        return new Pojo(table);
    }

    /**
     * 基本实体对象
     */
    protected Pojo pojo(String table, String cols) {
        Pojo pojo = new Pojo(table);
        pojo.setCols(cols);
        return pojo;
    }

    /**
     * 分页信息整合
     *
     * @param table    表名
     * @param pageInfo 分页信息（页码|页容量）
     */
    protected Page page(String table, Pojo pageInfo) {
        return page(table, pageInfo, "*");
    }

    /**
     * 分页信息整合
     *
     * @param table    表名
     * @param pageInfo 分页信息（页码|页容量）
     * @param cols     返回固定字段
     */
    protected Page page(String table, Pojo pageInfo, String cols) {
        int start = 1;
        int size = 10;
        if (pageInfo.get("pageNow") != null && pageInfo.get("pageNow").toString().matches("[\\d]+"))
            start = Integer.valueOf(pageInfo.get("pageNow").toString());
        if (pageInfo.get("pageSize") != null && pageInfo.get("pageSize").toString().matches("[\\d]+"))
            size = Integer.valueOf(pageInfo.get("pageSize").toString());
        Page page = new Page(table, start, size, cols);
        if (pageInfo.get("order") != null) {
            page.setOrder(pageInfo.get("order").toString());
            if (pageInfo.get("sort") != null)
                page.setSort(pageInfo.get("sort").toString());
        }
        if (pageInfo.get("search") != null)
            page.setSearchVal(pageInfo.get("search").toString());
        return page;
    }
}
