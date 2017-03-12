package com.mifuns.admin.controller;

import com.mifuns.system.facade.entity.SysUser;
import com.mifuns.system.facade.service.SysUserService;
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
    SysUserService sysUserService;


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "/security/login";
    }

    @RequestMapping(value = "/login")
    public String login(Model model, String username, String password){
        if(SecurityUtils.getSubject().isAuthenticated()){
            return  ("redirect:/index");
        }

        SysUser sysUser = sysUserService.queryUserByPassword(username,password);
        if(null == sysUser){
            return "/security/login";
        }

        return "";
    }



}
