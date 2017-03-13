package com.mifuns.admin.controller.system;

import com.github.pagehelper.Page;
import com.mifuns.system.facade.entity.Role;
import com.mifuns.system.facade.entity.User;
import com.mifuns.system.facade.service.RoleService;
import com.mifuns.system.facade.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by miguangying on 2017/3/3.
 */
@Controller()
@RequestMapping("/system/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @javax.annotation.Resource
    RoleService roleService;

    @javax.annotation.Resource
    UserService userService;

    @ResponseBody
    @RequestMapping("/list/data")
    public Object queryRoleList(){
        Role role = new Role();
        Page<Role> roles = roleService.queryPageList(role);

        User user = new User();
        Page<User> users = userService.queryPageList(user);
        logger.info("查询Role分页内容:{}",roles);
        logger.info("查询User分页内容:{}",users);
        return roles;
    }
}
