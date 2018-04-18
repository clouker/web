package org.clc.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.utils.Page;

import java.sql.*;
import java.util.Properties;

@Slf4j
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class,}))
public class MybatisInterceptor implements Interceptor {

	/*
	 * 拦截器要执行的方法
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		String id = mappedStatement.getId();
		Connection connection = (Connection) invocation.getArgs()[0];
		BoundSql boundSql = statementHandler.getBoundSql();
		if (id.matches(".+ByPage$") && boundSql.getParameterObject() instanceof Page) {// 拦截分页方法
			startPage(connection, boundSql, metaObject);
		} else if (id.matches("\\S+.insert\\w*") && boundSql.getParameterObject() instanceof Pojo) {
			ResultSet catalogs = connection.getMetaData().getColumns(null, "%", "SYS_USER", "%");
			while (catalogs.next()) {
				if (catalogs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("yes"))
					continue;
				String column_name = catalogs.getString("COLUMN_NAME");
				String type_name = catalogs.getString("TYPE_NAME");
				int column_size = catalogs.getInt("COLUMN_SIZE");
				System.out.println("column_name: " + column_name + "\ttype_name: " + type_name + "\tcolumn_size:" + column_size);
			}
			Pojo pojo = (Pojo) boundSql.getParameterObject();
			goInsert();
		}
		return invocation.proceed();
	}


	//region 拦截器需要拦截的对象
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	//endregion

	//region 设置初始化的属性值
	@Override
	public void setProperties(Properties properties) {

	}
	//endregion

	/**
	 * 分页业务处理
	 *
	 * @param connection
	 * @param boundSql
	 * @param metaObject
	 * @throws SQLException
	 */
	private void startPage(Connection connection, BoundSql boundSql, MetaObject metaObject) throws SQLException {
		Page page = (Page) boundSql.getParameterObject();
		PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(0) FROM " + page.getTable());
		ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
		parameterHandler.setParameters(countStatement);
		ResultSet rs = countStatement.executeQuery();
		if (rs.next())
			page.setRowCount(rs.getInt(1));
		log.info("<==      Total: " + page.getRowCount());
		// 拼装分页sql
		String _sql = "SELECT " + page.getCols() + " " + boundSql.getSql() + " limit " + page.start() + "," + page.getPageSize();
		metaObject.setValue("delegate.boundSql.sql", _sql);
	}

	private void goInsert() {
	}
}  