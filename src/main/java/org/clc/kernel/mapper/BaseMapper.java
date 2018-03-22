package org.clc.kernel.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface BaseMapper {
    HashMap findUserOne(int id);
}
