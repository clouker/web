package org.clc.web.controller.system;

import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.clc.utils.PathCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Autowired
    private BaseMapper menuMapper;

    @GetMapping("index")
    public String index() {
        return PathCode.MENU + PathCode.Index;
    }
}
