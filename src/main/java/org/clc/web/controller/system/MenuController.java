package org.clc.web.controller.system;

import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.clc.common.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("menu")
public class MenuController extends BaseController {

    private final BaseMapper menuMapper;

    @Autowired
    public MenuController(BaseMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @GetMapping("index")
    public String index() {
        return Path.MENU + Path.Index;
    }
}
