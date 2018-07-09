package org.clc.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.clc.kernel.mysql.pojo.Pojo;

import java.util.List;

public class Page {

    // -----------------------json过滤项----------------------- //
    @JsonIgnore// 表名
    private String table;
    @JsonIgnore// 查询返回字段
    private String cols = "*";
    @JsonIgnore// 搜索匹配字段 e.g."key1,key2"
    private String searchKeys = "NAME";
    @JsonIgnore// 搜索条件
    private String searchVal = "";
    @JsonIgnore// 分页条件
    private String where = "";
    @JsonIgnore// 分页排序
    private String order = "";
    @JsonIgnore// 分页排序(正反)
    private String sort = "desc";
    @JsonIgnore// 数据库类型 默认:mysql
    private String dialect ="mysql";
    // -----------------------返回项----------------------- //
    private int pageNow;// 跳转页码
    private int pageSize;// 单页容量
    private int rowCount;// 总记录
    private int pageCount;// 总页数
    private List<Pojo> records;// 数据集

    public Page(String table, int start, int size) {
        this.table = table;
        this.pageNow = start;
        this.pageSize = size;
    }

    public Page(String table, int start, int size, String dialect) {
        this.table = table;
        this.pageNow = start;
        this.pageSize = size;
        this.dialect = dialect;
    }

    // 获取记录起始位置
    public int start() {
        return (pageNow - 1) * pageSize;
    }
    // 获取记录结束位置
    public int end(){
        return start() + pageSize;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public void setCols(String cols) {
        this.cols = cols;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public void setSearchKeys(String searchKeys) {
        this.searchKeys = searchKeys;
    }
    public void setSearchVal(String searchVal) {
        this.searchVal = searchVal;
    }
    public void setRecords(List<Pojo> records) {
        this.records = records;
    }
    public void setWhere(String where) {
        this.where = where;
    }
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    // 获取总页数
    public int getPageCount() {
        if (rowCount % pageSize == 0)
            return rowCount / pageSize;
        return (rowCount / pageSize) + 1;
    }
    public String getOrder() {
        return order;
    }
    public int getPageSize() {
        return pageSize;
    }
    public int getRowCount() {
        return rowCount;
    }
    public String getSearchKeys() {
        return searchKeys;
    }
    public String getSearchVal() {
        return searchVal;
    }
    public String getSort() {
        return sort;
    }
    public String getTable() {
        return table;
    }
    public String getWhere() {
        return where;
    }
    public List<Pojo> getRecords() {
        return records;
    }
    public String getDialect() {
        return dialect;
    }
}
