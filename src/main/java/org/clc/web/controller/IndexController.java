package org.clc.web.controller;

import io.swagger.annotations.ApiOperation;
import org.clc.common.base.annotation.ActionLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {

    @ActionLog(value = "擦擦擦擦")
    @RequestMapping("/")
    @ApiOperation(value = "首页", notes = "项目首页")
    public String test(Model model) {
        return "index";
    }

    @ActionLog(name = "擦擦擦擦")
    @RequestMapping({"/1"})
    @ApiOperation(value = "首页", notes = "项目首页")
    public String test1(Model model) {
        return "index";
    }

    @ActionLog(value = "草草草草", name = "水水水水", description = "擦擦擦擦")
    @RequestMapping({"/2"})
    @ApiOperation(value = "首页", notes = "项目首页")
    public String test2(Model model) {
        return "index";
    }
}
