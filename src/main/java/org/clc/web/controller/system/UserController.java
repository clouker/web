package org.clc.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.clc.common.Path;
import org.clc.pojo.Result;
import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.pojo.Pojo;
import org.clc.pojo.Page;
import org.clc.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(value = "用户管理", tags = "系统用户")
@RequestMapping("user")
public class UserController extends BaseController {

    private static String table = "user";

    private final BaseMapper userMapper;

    @Autowired
    public UserController(BaseMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping(value = {"index", ""})
    @ApiOperation(value = "用户列表UI")
    public String index(Model model) {
        model.addAttribute("user", "linb");
        return Path.USER + Path.Index;
    }

    @ResponseBody
    @PostMapping("findByPage")
    @ApiOperation(value = "用户分页信息")
    @ApiImplicitParam(name = "分页信息", value = "Page", dataType = "Page")
    public Result findByPage() throws Exception {
        Page page = page(table, getParams());
//		page.setWhere("ID != '80'");
        page.setSearchKeys("NAME");
        page.setRecords(userMapper.findByPage(page));
        return Result.success(page);
    }

    @ResponseBody
    @Transactional
    @PostMapping("add")
    @ApiOperation(value = "用户添加")
    @ApiImplicitParam(name = "用户信息", value = "Pojo", dataType = "Pojo")
    public Result add() throws Exception {
        Pojo pojo = getParams();
        if (pojo.size() > 0) {
            pojo.setTable(table);
            int code = userMapper.insert(pojo);
            if (code > 0)
                return Result.success();
            return Result.error("添加失败,请稍后重试...");
        }
        return Result.error("参数有误...");
    }
}
