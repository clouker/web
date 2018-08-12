package org.clc.kernel.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clc.pojo.Page;
import org.clc.kernel.mysql.pojo.Pojo;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseMapper {

    @Select("select * from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    List<Map> listTable();

    @Select("select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")
    List<Map> listTableColumn(String tableName);

    @Select("SHOW DATABASES")
    List<Map> listDatabases();

    /**
     * 查询
     *
     * @param pojo(必传tableName) 支持：
     *                          where --> pojo.put("where", "where条件")
     *                          order --> pojo.put("order", "order条件")
     *                          sort  --> pojo.put("sort","ase|desc")
     * @return
     */
    List<Pojo> find(Pojo pojo);

    List<Pojo> findByPage(Page page);

    int insert(Pojo pojo);

    List<Map> initTablesInfo();
}
