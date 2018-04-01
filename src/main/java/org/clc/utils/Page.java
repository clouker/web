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
    private int pageNow;
    @NonNull// 单页容量
    private int pageSize;
    // 查询字段
    private String cols = "*";
    // 分页条件
    private String where = "";
    // 分页排序
    private String order = "";
    // 分页排序(正反)
    private String sort = "desc";
    // 总记录
    private int rowCount;
    // 总页数
    private int pageCount;
    // 数据集
    private List<Pojo> records;

    // 清除与分页无关信息
    public void clear() {
        this.table = "";
        this.cols = "";
        this.where = "";
        this.sort = "";
        this.order = "";
    }

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
