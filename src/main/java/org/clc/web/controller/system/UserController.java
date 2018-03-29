package org.clc.web.controller.system;

import org.clc.utils.Page;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.clc.utils.PathCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private BaseMapper userMapper;

    @GetMapping(value = "index")
    String find() {
        return PathCode.USER + PathCode.Index;
    }

    @PostMapping("findByPage")
    @ResponseBody
    Page getUser() {
        Pojo pageInfo = getPojo("SYS_USER");
        Page page = page("SYS_USER", pageInfo);
        page.setWhere("USER_ID != '80'");
        page.setOrder("USER_NAME");
        List<Pojo> users = userMapper.findByPage(page);
        page.setRecords(users);
        return page;
    }
}
