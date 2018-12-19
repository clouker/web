package org.clc.kernel.mysql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.clc.pojo.Pojo;
import org.clc.pojo.Page;

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
     */
    @Select("select ${cols} from ${table}")
    List<Pojo> find(Pojo pojo);

    @Select("select ${cols} from ${table}")
    List<Pojo> findByPage(Page page);

    @Insert(" insert into ${table} ")
    int insert(Pojo pojo);
}
