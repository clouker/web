package org.clc.common;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.clc.kernel.pojo.Pojo;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Page {

    @NonNull// 表名
    private String table;
    @NonNull// 跳转页码
    private int start;
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
    // 数据集
    private List<Pojo> list;

    // 获取记录起始位置
    public int getStart() {
        return (start - 1) * size;
    }
}
