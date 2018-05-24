package org.clc.web.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {

//    @Autowired
//    private MongoBaseDao dao;

    @RequestMapping({"/"})
    @ApiOperation(value = "首页", notes = "项目首页")
    public String index(Model model) {
//        FindIterable<Document> documents = dao.getCollection().find();
//        model.addAttribute("documents", documents);
        return "index";
    }
}
