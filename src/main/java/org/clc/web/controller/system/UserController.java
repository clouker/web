package org.clc.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.clc.common.Result;
import org.clc.common.annotation.Log;
import org.clc.pojo.Page;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.utils.RedisUtil;
import org.clc.web.controller.BaseController;
import org.clc.common.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(value = "用户管理", tags = "系统用户")
@RequestMapping("user")
public class UserController extends BaseController {

    private static String table = "sys_user";
    private static int i = 0;

    @Autowired
    private BaseMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping(value = {"index", ""})
    @ApiOperation(value = "用户列表UI")
    public String index(Model model) {
        model.addAttribute("user", "linb");
        return Path.USER + Path.Index;
    }

    @Log(value = "name")
    @ResponseBody
    @PostMapping("findByPage")
    @ApiOperation(value = "用户分页信息")
    @ApiImplicitParam(name = "分页信息", value = "Page", dataType = "Page")
    public Page<Pojo> findByPage() throws Exception {
        Page<Pojo> page = page(table, getPojo());
        if (redisUtil.hasKey(page.getTable() + "." + page.getPageNow() + "-" + page.getPageSize()))
            page = (Page<Pojo>) redisUtil.hget("table", page.getTable() + "*" + page.getPageNow() + "-" + page.getPageSize());
//		page.setWhere("ID != '80'");
        page.setSearchKeys("NAME");
        page.setRecords(userMapper.findByPage(page));
        redisUtil.hset("table", page.getTable() + "." + page.getPageNow() + "-" + page.getPageSize(), page);
        return page;
    }

    @ResponseBody
    @Transactional
    @PostMapping("add")
    @ApiOperation(value = "用户添加")
    @ApiImplicitParam(name = "用户信息", value = "Pojo", dataType = "Pojo")
    public Result add() throws Exception {
        Pojo pojo = getPojo();
        if (pojo.size() > 0) {
            pojo.setTable(table);
            int code = userMapper.insert(pojo);
            if (code > 0)
                return Result.success();
        }
        return Result.error();
    }
}
