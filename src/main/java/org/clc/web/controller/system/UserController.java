package org.clc.web.controller.system;

import org.clc.common.Page;
import org.clc.kernel.pojo.Pojo;
import org.clc.kernel.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.clc.web.utils.PathCode;
import org.clc.web.utils.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private BaseMapper userMapper;

    @GetMapping(value = "find")
    String find(Model model){
        Pojo p = getPojo("SYS_USER");
        System.out.println(p);
        Page page = page("SYS_USER", 1, 10);
        page.setWhere("USER_ID != 8");
        page.setOrder("USER_NAME");
        List<Pojo> users = userMapper.findByPage(page);
        model.addAttribute("user", users);
        return PathCode.USER + PathCode.Index;
    }
}
