package com.mifuns.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by miguangying on 2016/12/22.
 */
@Controller
public class IndexController {


    @RequestMapping("/index")
    public String index(){
        return "/index";
    }
}
