package com.mifuns.admin.controller;

import com.mifuns.system.facade.entity.User;
import com.mifuns.system.facade.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by miguangying on 2016/12/22.
 */
@Controller
public class IndexController {

    @Resource
    UserService userService;


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "/security/login";
    }

    @RequestMapping(value = "/login")
    public String login(Model model, String username, String password){
        if(SecurityUtils.getSubject().isAuthenticated()){
            return  ("redirect:/index");
        }

        User user = userService.queryUserByPassword(username,password);
        if(null == user){
            return "/security/login";
        }

        return "";
    }



}
