package org.clc.utils;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.clc.kernel.mysql.pojo.Pojo;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Page {

    @NonNull// 表名
    private String table;
    @NonNull// 跳转页码
    private int now;
    @NonNull// 单页容量
    private int size;
    // 查询字段
    private String cols = "*";
    // 分页条件
    private String where = "";
    // 分页排序
    private String order = "";
    // 分页排序(正反)
    private String sort = "";
    // 总记录
    private int total;
    // 总页数
    private int count;
    // 数据集
    private List<Pojo> records;


    // 获取记录起始位置
    public int start() {
        return (now - 1) * size;
    }

    // 获取总页数
    public int getCount() {
        if (total % size == 0)
            return total / size;
        return (total / size) + 1;
    }
}
