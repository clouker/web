package org.clc.common.mybatisPlugin;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

@Intercepts({
		@Signature(
				type = StatementHandler.class,
				method = "prepare",
				args = {Connection.class, Integer.class}),
		@Signature(
				type = ResultSetHandler.class,
				method = "handleResultSets",
				args = {Statement.class})})
public class MybatisInterceptor implements Interceptor {

	/*
	 * 拦截器要执行的方法
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		String method = invocation.getMethod().toGenericString();
		if (method.contains("StatementHandler.prepare"))// sql执行前拦截
			MyBatisBeforeInterceptor.run(invocation);
		else if (method.contains("ResultSetHandler.handleResultSets"))// sql执行后拦截
			return MyBatisAfterInterceptor.run(invocation);
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
}