package org.clc.web.controller.system;

import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.clc.utils.PathCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Resource
    private BaseMapper menuMapper;

    @GetMapping("index")
    String index() {
        return PathCode.MENU + PathCode.Index;
    }
}
