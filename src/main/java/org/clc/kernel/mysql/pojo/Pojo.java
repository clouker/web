package org.clc.kernel.mysql.pojo;

import lombok.Data;

import java.util.HashMap;

/**
 * 实体类
 */
@Data
public class Pojo extends HashMap<String, Object> {

    // 表名
    private String table = "table";
    // 查询字段
    private String cols = "*";


    public Pojo put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Pojo() {
    }

    public Pojo(String table) {
        this.table = table;
    }
}



