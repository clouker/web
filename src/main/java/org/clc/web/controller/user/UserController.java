package org.clc.web.controller.user;

import org.clc.kernel.entity.User;
import org.clc.kernel.repository.UserRepository;
import org.clc.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("findByName")
    @ResponseBody
    User findByName(){
        return userRepository.findByName("admin");
    }

    /**
     * 根据一组Id查数据
     * @return
     */
    @RequestMapping("findByIds")
    @ResponseBody
    List<User> findByWhere(){
        return userRepository.findByIdIsInAndDeleteFlagIs(new Integer[]{1, 2},0);
    }
}
