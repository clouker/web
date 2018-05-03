package org.clc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.utils.Page;
import org.clc.utils.RequestUtil;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class BaseController {


	//日志记录
	protected static Logger logger = log;

	/**
	 * 获取请求实体
	 */
	protected Pojo getPojo() throws Exception {
		Pojo pojo = null;
		HttpServletRequest request = RequestUtil.getRequest();
		String contentType = request.getContentType();
		if (contentType != null) {
			if (contentType.startsWith("application/json")) {
				StringBuffer jb = new StringBuffer();
				String line;
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
				/** jackson---JSON<==>POJO 操作类为ObjectMapper
				 * Class2JSON
				 * @writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
				 * @writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
				 * @writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
				 * @writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串
				 *   pojo = {"name":"小民","age":20}
				 *       pojo对象转JSON----mapper.writeValueAsString(pojo)
				 *   list = [{"name":"小民","age":20}]
				 *       list集合转JSON---mapper.writeValueAsString(list)
				 * JSON2Class
				 * @ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化
				 *   json = "{'name':'小民','age':20,}";
				 *       mapper.readValue(json, Pojo.class)
				 */
				ObjectMapper mapper = new ObjectMapper();
				pojo = mapper.readValue(jb.toString(), Pojo.class);
			} else {
				Map<String, String[]> map = request.getParameterMap();
				Pojo pojo1 = new Pojo();
				map.forEach((k, v) -> {
					StringBuffer sb = new StringBuffer();
					if (v.length == 1)
						sb.append(v[0]);
					else// 单key多val时，v以，相连
						for (String s : v)
							sb.append(s + ",");
					pojo1.put(k, sb.toString());
				});
				if (pojo1.size() > 0)
					pojo = pojo1;
				// 判断是否包含上传文件
				if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
					Collection<Part> parts = request.getParts();
					Pojo pojo2 = new Pojo();
					parts.forEach(item -> {
						if (item.getSubmittedFileName() != null) {
							try {
								pojo2.put(item.getName(), IOUtils.toByteArray(item.getInputStream()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					if (pojo2.size() > 0) {
						if (pojo.size() > 0)
							pojo.putAll(pojo2);
						else
							pojo = pojo2;
					}
				}
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
	 * @param table    表名
	 * @param pageInfo 分页信息（页码|页容量）
	 * @return
	 */
	protected Page page(String table, Pojo pageInfo) {
		return page(table, "*", pageInfo);
	}

	/**
	 * 分页信息整合
	 *
	 * @param table    表名
	 * @param cols     返回固定字段
	 * @param pageInfo 分页信息（页码|页容量）
	 * @return
	 */
	protected Page page(String table, String cols, Pojo pageInfo) {
		int start = 1;
		int size = 10;
		if (pageInfo.get("pageNow") != null && pageInfo.get("pageNow").toString().matches("[\\d]+"))
			start = Integer.valueOf(pageInfo.get("pageNow").toString());
		if (pageInfo.get("pageSize") != null && pageInfo.get("pageSize").toString().matches("[\\d]+"))
			size = Integer.valueOf(pageInfo.get("pageSize").toString());
		Page page = new Page(table, start, size);
		if (pageInfo.get("order") != null)
			page.setOrder(pageInfo.get("order").toString());
		if (pageInfo.get("sort") != null)
			page.setSort(pageInfo.get("sort").toString());
		if (pageInfo.get("search") != null)
			page.setSearchVal(pageInfo.get("search").toString());
		page.setCols(cols);
		return page;
	}
}
