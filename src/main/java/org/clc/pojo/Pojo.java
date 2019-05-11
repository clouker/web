package org.clc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 实体类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pojo extends HashMap<String, Object> implements Serializable {

    @JsonIgnore// 表名
    private String table = "table";
    @JsonIgnore// 查询字段
    private String cols = "*";
    @JsonIgnore// 数据库类型 默认:mysql
    private String dialect = "mysql";

    public Pojo put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Pojo() {
    }

    public Pojo(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public String getDialect() {
        return dialect;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

}



