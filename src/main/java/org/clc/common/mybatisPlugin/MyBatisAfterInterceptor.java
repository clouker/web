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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行后拦截
 *
 * @author linb
 */
@Slf4j
public class MyBatisAfterInterceptor {

	public static void run(Invocation invocation) throws SQLException {
		Object[] args = invocation.getArgs();
		// 获取到当前的Statement
		Statement stmt = (Statement) args[0];
		// 通过Statement获得当前结果集
		ResultSet resultSet = stmt.getResultSet();
		ResultSetMetaData metaData = resultSet.getMetaData();
		String tableName = metaData.getTableName(1);
		System.out.println(tableName + metaData.getColumnCount());
		StringBuilder sb = new StringBuilder(0);
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			sb.append(" ColumnName: " + metaData.getColumnName(i));
			sb.append(" ColumnType: " + metaData.getColumnTypeName(i));
			sb.append(" Precision: " + metaData.getPrecision(i) + "\n");
		}
		System.out.println(resultSet.getFetchSize());
		List<Object> resultList = new ArrayList<>();
		if (resultSet != null && resultSet.next()) {
			while (resultSet.next()) {

				String update_times = resultSet.getString("UPDATE_TIME");
				System.out.println(update_times);
			}
		}
	}
}
