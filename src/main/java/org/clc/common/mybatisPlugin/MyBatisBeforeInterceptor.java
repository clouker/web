package org.clc.common.mybatisPlugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.utils.Page;
import org.clc.utils.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行前拦截
 * @author linb
 */
@Slf4j
public class MyBatisBeforeInterceptor {

	public static void run(Invocation invocation) throws SQLException {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		String id = mappedStatement.getId();
		Connection connection = (Connection) invocation.getArgs()[0];
		BoundSql boundSql = statementHandler.getBoundSql();
		String sql = "";
		if (id.matches(".+ByPage$") && boundSql.getParameterObject() instanceof Page) {// 拦截分页方法
			sql = startPage(connection, boundSql, metaObject);
		} else if (id.matches("\\S+.insert\\w*") && boundSql.getParameterObject() instanceof Pojo) {
			Pojo pojo = (Pojo) boundSql.getParameterObject();
			Map<String, Object> cols = new HashMap<>();
			ResultSet catalogs = connection.getMetaData().getColumns(null, "%", "SYS_USER", "%");
			while (catalogs.next()) {
				if (catalogs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("yes"))
					continue;
				String column_name = catalogs.getString("COLUMN_NAME");
				String column = "$" + StringUtil.underline2camel(column_name);
				if (pojo.containsKey(column))
					cols.put(column_name, pojo.get(column));
			}
			sql = goInsert(cols, pojo.getTable());
		}
		metaObject.setValue("delegate.boundSql.sql", sql);
	}

	/**
	 * 分页业务处理
	 *
	 * @param connection
	 * @param boundSql
	 * @param metaObject
	 * @throws SQLException
	 */
	private static  String startPage(Connection connection, BoundSql boundSql, MetaObject metaObject) throws SQLException {
		Page page = (Page) boundSql.getParameterObject();
		//统计sql
		StringBuilder countSql = new StringBuilder("SELECT COUNT(0) FROM " + page.getTable());
		if (page.getWhere().length() > 0)
			countSql.append(" where " + page.getWhere());
		StringBuilder search = new StringBuilder(0);
		if (page.getSearchVal().length() > 0) {
			String[] searchKeys = page.getSearchKeys().split(",");
			for (String searchKey : searchKeys)
				search.append(searchKey + " like '%" + page.getSearchVal() + "%' and ");
			if (countSql.indexOf("where") != -1)
				countSql.append(" and " + search.substring(0, search.length() - 5));
			else
				countSql.append(" where " + search.substring(0, search.length() - 5));
		}
		PreparedStatement countStatement = connection.prepareStatement(countSql.toString());
		ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
		parameterHandler.setParameters(countStatement);
		ResultSet rs = countStatement.executeQuery();
		if (rs.next())
			page.setRowCount(rs.getInt(1));
		log.info("<==      Total: " + page.getRowCount());
		// 拼装分页sql
		StringBuilder sql = new StringBuilder(boundSql.getSql());
		if (page.getWhere().length() > 0)
			sql.append(" where " + page.getWhere());
		if (search.length() > 0) {
			if (sql.indexOf("where") != -1)
				sql.append(" and " + search.substring(0, search.length() - 5));
			else
				sql.append(" where " + search.substring(0, search.length() - 5));
		}
		if (page.getOrder().length() > 0) {
			sql.append(" order by " + page.getOrder());
			if (page.getSort().length() > 0)
				sql.append(" " + page.getSort());
		}

		sql.append(" limit " + page.start() + "," + page.getPageSize());
		return sql.toString();
	}

	/**
	 * 封装insert SQL
	 *
	 * @param cols
	 * @return
	 */
	private static String goInsert(Map<String, Object> cols, String table) {
		StringBuilder sql = new StringBuilder("insert into " + table);
		StringBuilder key = new StringBuilder("(");
		StringBuilder val = new StringBuilder("values(");
		cols.forEach((k, v) -> {
			key.append(k + ",");
			val.append("'" + v + "',");
		});
		sql.append(key.deleteCharAt(key.lastIndexOf(",")).append(") "));
		sql.append(val.deleteCharAt(val.lastIndexOf(",")).append(")"));
		return sql.toString();
	}
}
