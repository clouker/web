package org.clc.common.datasource.mybatis.Plugin;

import org.apache.ibatis.plugin.Invocation;
import org.clc.utils.StringUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行后拦截
 *
 * @author linb
 */
class MyBatisAfterInterceptor {

    static Object run(Invocation invocation) throws Exception {
        // 获取当前Statement
        Statement stmt = (Statement) invocation.getArgs()[0];
        // 通过Statement获得当前结果集
        ResultSet resultSet = stmt.getResultSet();
        if (resultSet != null) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Map<String, String>> keys = new ArrayList<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
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
     * @param keys   字段和类型
     * @param resultSet
     * @return
     */
    private static List<Map<String, java.io.Serializable>> getResult(List<Map<String, String>> keys, ResultSet resultSet) throws Exception {
        List<Map<String, java.io.Serializable>> resultList = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, java.io.Serializable> cols = new HashMap<>();
            keys.forEach(key -> {
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
                    default:
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
