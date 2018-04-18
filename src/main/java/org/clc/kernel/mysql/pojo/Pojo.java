package org.clc.kernel.mysql.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;

/**
 * 实体类
 */
@Data
public class Pojo extends HashMap<String, Object> {

    @JsonIgnore// 表名
    private String table = "table";
    @JsonIgnore// 查询字段
    private String cols = "*";
    // 跳转页码
    private int pageNow;
    // 单页容量
    private int pageSize;

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



