package org.clc.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.clc.common.Info;
import org.clc.utils.Page;
import org.clc.kernel.mysql.pojo.Pojo;
import org.clc.kernel.mysql.mapper.BaseMapper;
import org.clc.web.controller.BaseController;
import org.clc.common.PathCode;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private BaseMapper userMapper;

	@GetMapping(value = {"index", ""})
	@ApiOperation(value = "用户列表UI")
	public String index(Model model) {
		model.addAttribute("user", "linb");
		return PathCode.USER + PathCode.Index;
	}

	@ResponseBody
	@PostMapping("findByPage")
	@ApiOperation(value = "用户分页信息")
	@ApiImplicitParam(name = "分页信息", value = "Page", dataType = "Page")
	public Page findByPage() {
		Page page = page("SYS_USER", getPojo());
		page.setWhere("USER_ID != '80'");
		page.setSearchKeys("NAME");
		List<Pojo> users = userMapper.findByPage(page);
		page.setRecords(users);
		return page;
	}

	@PostMapping("add")
	@Transactional
	public Info add() {
		Pojo pojo = getPojo();
		pojo.setTable("SYS_USER");
		int code = userMapper.insert(pojo);
		return Info.returnMsg(0, "Success");
	}
}
