package org.clc.kernel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.clc.common.Page;
import org.clc.kernel.pojo.Pojo;

import java.util.List;

@Mapper
public interface BaseMapper {

    /**
     * 查询
     * @param pojo
     * 支持：
     *      where --> pojo.put("where", "where条件")
     *      order --> pojo.put("order", "order条件")
     * @return
     */
    List<Pojo> find(Pojo pojo);

    List<Pojo> findByPage(Pojo pojo);
}
