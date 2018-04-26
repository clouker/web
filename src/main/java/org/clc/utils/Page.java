package org.clc.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.clc.kernel.mysql.pojo.Pojo;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Page {

    @NonNull@JsonIgnore// 表名
    private String table;
    @NonNull// 跳转页码
    private int pageNow;
    @NonNull// 单页容量
    private int pageSize;
    @JsonIgnore// 查询返回字段
    private String cols = "*";
    @JsonIgnore// 搜索匹配字段--"key1,key2"
    private String searchKeys = "NAME";
    @JsonIgnore// 搜索条件
    private String searchVal = "";
    @JsonIgnore// 分页条件
    private String where = "";
    @JsonIgnore// 分页排序
    private String order = "";
    @JsonIgnore// 分页排序(正反)
    private String sort = "desc";
    // 总记录
    private int rowCount;
    // 总页数
    private int pageCount;
    // 数据集
    private List<Pojo> records;
    // 获取记录起始位置
    public int start() {
        return (pageNow - 1) * pageSize;
    }
    // 获取总页数
    public int getPageCount() {
        if (rowCount % pageSize == 0)
            return rowCount / pageSize;
        return (rowCount / pageSize) + 1;
    }
}
