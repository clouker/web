package org.clc.common.datasource.mybatis.Plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.clc.pojo.Pojo;
import org.clc.pojo.Page;
import org.clc.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行前拦截
 *
 * @author linb
 */
class MyBatisBeforeInterceptor {

    private static Logger log = LoggerFactory.getLogger(MyBatisBeforeInterceptor.class);

    static void run(Invocation invocation) throws SQLException {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String id = mappedStatement.getId();
        Connection connection = (Connection) invocation.getArgs()[0];
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql;
        if (id.matches(".+ByPage$") && boundSql.getParameterObject() instanceof Page)// 拦截分页方法
            sql = startPage(connection, boundSql, metaObject);
        else if (id.matches("\\S+.insert\\w*") && boundSql.getParameterObject() instanceof Pojo) {
            Pojo pojo = (Pojo) boundSql.getParameterObject();
            Map<String, Object> cols = new HashMap<>();
            ResultSet catalogs = connection.getMetaData().getColumns(null,
                    "%", pojo.getTable(), "%");
            while (catalogs.next()) {
                if (catalogs.getString("IS_AUTOINCREMENT").equalsIgnoreCase("yes"))
                    continue;
                String column_name = catalogs.getString("COLUMN_NAME");
                String column = "$" + StringUtil.underline2camel(column_name);
                if (pojo.containsKey(column))
                    cols.put(column_name, pojo.get(column));
            }
            sql = goInsert(cols, pojo.getTable());
        } else if (id.matches("\\S+.update\\w*")) {
            sql = goUpdate();
        } else sql = boundSql.getSql();
        metaObject.setValue("delegate.boundSql.sql", sql);
    }

    private static String goUpdate() {
        return "";
    }

    /**
     * 分页业务处理
     */
    private static String startPage(Connection connection, BoundSql boundSql, MetaObject metaObject) throws SQLException {
        Page page = (Page) boundSql.getParameterObject();
        // 统计sql
        StringBuilder countSql = new StringBuilder("SELECT count(0) FROM " + page.getTable());
        if (page.getWhere().length() > 0)
            countSql.append(" WHERE ").append(page.getWhere());
        StringBuilder search = new StringBuilder(0);
        if (page.getSearchVal().length() > 0) {
            String[] searchKeys = page.getSearchKeys().split(",");
            for (String searchKey : searchKeys)
                search.append(searchKey).append(" like '%").append(page.getSearchVal()).append("%' and ");
            if (countSql.indexOf("WHERE") != -1)
                countSql.append(" and ").append(search.substring(0, search.length() - 5));
            else
                countSql.append(" WHERE ").append(search.substring(0, search.length() - 5));
        }
        PreparedStatement countStatement = connection.prepareStatement(countSql.toString());
        ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
        parameterHandler.setParameters(countStatement);
        ResultSet rs = countStatement.executeQuery();
        if (rs.next())
            page.setRowCount(rs.getInt(1));
        log.info("<==   Total: " + page.getRowCount());
        // 拼装分页sql
        StringBuilder sql = new StringBuilder(boundSql.getSql());
        if (page.getWhere().length() > 0)
            sql.append(" WHERE ").append(page.getWhere());
        if (search.length() > 0) {
            if (sql.indexOf("WHERE") != -1)
                sql.append(" and ").append(search.substring(0, search.length() - 5));
            else
                sql.append(" WHERE ").append(search.substring(0, search.length() - 5));
        }
        if (page.getOrder().length() > 0) {
            sql.append(" ORDER BY ").append(page.getOrder());
            if (page.getSort().length() > 0)
                sql.append(" ").append(page.getSort());
        }
        return getPageSql(sql, page);
    }

    /**
     * 封装分页sql - 多数据库支持
     */
    private static String getPageSql(StringBuilder sql, Page page) {
        StringBuilder pageSql = new StringBuilder(0);
        switch (page.getDialect()) {
            case "postgresql":
                pageSql.append(sql);
                pageSql.append(" limit ").append(page.start()).append(",").append(page.getPageSize());
                break;
            case "hsqldb":
                pageSql.append(sql);
                pageSql.append(" limit ").append(page.start()).append(",").append(page.getPageSize());
                break;
            case "oracle":
                pageSql.append("select * from ( select temp.*, rownum row_id from ( ").append(sql);
                pageSql.append(" ) temp where rownum <= ").append(page.end());
                pageSql.append(") where row_id > ").append(page.start());
                break;
            case "mysql":
            default:
                pageSql.append(sql);
                pageSql.append(" limit ").append(page.start()).append(",").append(page.getPageSize());
                break;
        }
        return pageSql.toString();
    }

    /**
     * 封装insert SQL
     */
    private static String goInsert(Map<String, Object> cols, String table) {
        StringBuilder sql = new StringBuilder("insert into " + table);
        StringBuilder key = new StringBuilder("(");
        StringBuilder val = new StringBuilder(" values(");
        cols.forEach((k, v) -> {
            key.append(k).append(",");
            val.append("'").append(v).append("',");
        });
        sql.append(key.deleteCharAt(key.lastIndexOf(",")).append(")"));
        sql.append(val.deleteCharAt(val.lastIndexOf(",")).append(")"));
        return sql.toString();
    }
}
