package org.clc.web.controller;

import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.kernel.mysql.pojo.Pojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dbInfo")
public class DBController {


    private Logger log = LoggerFactory.getLogger(DBController.class);

    @Autowired
    private BaseMapper baseMapper;

    @GetMapping(value = "/{table}")
    public List<Pojo> tableInfo(@PathVariable String table){
        log.info(table);
        return Collections.singletonList(new Pojo(table));
    }

    @GetMapping
    public List<?> getDBs(){
        List<Map> list = baseMapper.listDatabases();
        list.forEach(System.out::println);
        return list;
    }

    @GetMapping("getAllTable")
    public List<?> getAllTable(){
        List<Map> list = baseMapper.listTable();
        list.forEach(System.out::println);
        return list;
    }
}
