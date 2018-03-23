package org.clc.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.clc.common.Page;

@Slf4j
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare",
        args = {Connection.class, Integer.class,}))
public class PageInterceptor implements Interceptor {

    /*
     * 拦截器要执行的方法
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String id = mappedStatement.getId();
        if (id.matches(".+ByPage$")) {// 拦截分页方法
            BoundSql boundSql = statementHandler.getBoundSql();
            Page page = (Page) boundSql.getParameterObject();
            Connection connection = (Connection) invocation.getArgs()[0];
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(0) FROM " + page.getTable());
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            parameterHandler.setParameters(countStatement);
            ResultSet rs = countStatement.executeQuery();
            if (rs.next())
                page.setTotal(rs.getInt(1));
              log.debug("<==      Total: " + page.getTotal());
            // 拼装分页sql
            String _sql = "SELECT " + page.getCols() + " " + boundSql.getSql() + " limit " + page.getStart() + "," + page.getTotal();
            metaObject.setValue("delegate.boundSql.sql", _sql);
        }
        return invocation.proceed();
    }

    /*
     * 拦截器需要拦截的对象
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /*
     * 设置初始化的属性值
     */
    @Override
    public void setProperties(Properties properties) {

    }
}  