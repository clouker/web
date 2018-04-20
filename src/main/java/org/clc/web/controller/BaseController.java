package org.clc.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.utils.Page;
import org.clc.utils.RequestUtil;
import org.slf4j.Logger;

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
	 */
	protected Pojo getPojo() {
		HttpServletRequest request = RequestUtil.getRequest();
		Map<String, String[]> map = request.getParameterMap();
		Pojo pojo = new Pojo();
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
		page.setCols(cols);
		return page;
	}
}
