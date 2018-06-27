package org.clc.task;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clc.kernel.mysql.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Api("InitDBTablesInfo")
public class DBTableInfo {

    @Autowired
    private BaseMapper baseMapper;

    @ApiOperation("initTableInfo")
    @Scheduled(cron = "10 * * * * ?")
    private void initTableInfo() {
        List<Map> pojos = baseMapper.initTablesInfo();
        System.out.println(pojos);
    }
}
