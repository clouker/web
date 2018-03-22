package org.clc.kernel.pojo;

import lombok.Data;

import java.util.HashMap;

/**
 * 实体类
 */
@Data
public class Pojo extends HashMap<String, Object> {

    @Override
    public Pojo put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    private Pojo() {
    }

    //指定表名&查询字段(默认*)
    public Pojo(String table) {
        this.put("table", table).put("Cols", "*");
    }

    //指定表名&查询字段(eg: "key1,key2")
    public Pojo(String table, String Cols) {
        this.put("table", table).put("Cols", Cols);
    }
}



