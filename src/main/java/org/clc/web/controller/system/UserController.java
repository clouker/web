package org.clc.web.controller.system;

import org.clc.kernel.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private BaseMapper userMapper;

    @GetMapping(value = "findAll")
    String findAll(Model model){
        HashMap user = userMapper.findUserOne(1);
        model.addAttribute("user",user);
        return "index";
    }
}
