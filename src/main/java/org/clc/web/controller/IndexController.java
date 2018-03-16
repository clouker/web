package org.clc.web.controller;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.clc.mongo.dao.MongoBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController{

    @Autowired
    private MongoBaseDao dao;

    @RequestMapping({"/","index"})
    String index(Model model){
//        try {
//            getEntity(java.util.HashMap.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FindIterable<Document> documents = dao.find(10,null);
//        System.out.println(documents);
//        model.addAttribute("documents",documents);
        return "index";
    }
}
