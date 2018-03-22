package org.clc.web.controller.system;

import org.clc.common.Page;
import org.clc.kernel.pojo.Pojo;
import org.clc.kernel.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
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
    String find(Model model) {

//        getPojo(Pojo.class);
        Pojo user = pojo("SYS_USER").put("where", "USER_ID = 1");
        List<Pojo> users = userMapper.findByPage(user);
        model.addAttribute("user", users);
        return "index";
    }
}
