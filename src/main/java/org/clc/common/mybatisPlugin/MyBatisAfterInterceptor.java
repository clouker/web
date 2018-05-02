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

    public static Object run(Invocation invocation) throws Exception {
        // 获取到当前的Statement
        Statement stmt = (Statement) invocation.getArgs()[0];
        // 通过Statement获得当前结果集
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Map<String, String>> keys = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                Map<String, String> col = new HashMap<>();
                col.put("name", metaData.getColumnName(i));
                col.put("type", metaData.getColumnTypeName(i));
                keys.add(col);
            }
            return getResult(keys, resultSet);
        }
        return invocation.proceed();
    }

    /**
     * 处理返回集
     *
     * @param keys      (表字段和类型)
     * @param resultSet
     * @return
     */
    private static List<Pojo> getResult(List<Map<String, String>> keys, ResultSet resultSet) throws SQLException {
        List<Pojo> resultList = new ArrayList<>();
        while (resultSet.next()) {
            Pojo cols = new Pojo();
            keys.forEach(key -> {
//				System.out.println(key.get("type"));
                // underline2camel
                String name = StringUtil.underline2camel(key.get("name"));
                switch (key.get("type")) {
//					case "BLOB":
//						try {
//							cols.put(name, resultSet.getBlob(key.get("name")));
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//						break;
                    case "TIMESTAMP":
                        try {
                            cols.put(name, resultSet.getTimestamp(key.get("name")));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:// null->""
                        try {
                            cols.put(name, resultSet.getString(key.get("name")) != null ? resultSet.getString(key.get("name")) : "");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
            });
            resultList.add(cols);
        }
        return resultList;
    }
}
