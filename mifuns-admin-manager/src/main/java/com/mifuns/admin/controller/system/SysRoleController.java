package com.mifuns.admin.controller.system;

import com.github.pagehelper.Page;
import com.mifuns.system.facade.entity.SysRole;
import com.mifuns.system.facade.entity.SysUser;
import com.mifuns.system.facade.service.SysRoleService;
import com.mifuns.system.facade.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by miguangying on 2017/3/3.
 */
@Controller()
@RequestMapping("/system/role")
public class SysRoleController {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);

    @Resource
    SysRoleService sysRoleService;

    @Resource
    SysUserService sysUserService;

    @ResponseBody
    @RequestMapping("/list/data")
    public Object queryRoleList(){
        SysRole sysRole = new SysRole();
        Page<SysRole> sysRoles = sysRoleService.queryPageList(sysRole);

        SysUser sysUser = new SysUser();
        Page<SysUser> sysUsers = sysUserService.queryPageList(sysUser);
        logger.info("查询SysRole分页内容:{}",sysRoles);
        logger.info("查询SysUser分页内容:{}",sysUsers);
        return sysRoles;
    }
}
