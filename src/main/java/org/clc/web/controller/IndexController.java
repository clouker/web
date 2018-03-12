package org.clc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController extends BaseController{

    @RequestMapping({"/","index"})
    String index(Model model){
        try {
//            getEntity(HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("totalData","hello");
        return "index";
    }
}
