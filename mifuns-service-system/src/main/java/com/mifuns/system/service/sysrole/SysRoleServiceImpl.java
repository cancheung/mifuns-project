package com.mifuns.system.service.sysrole;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.service.BaseCommonService;
import com.mifuns.system.facade.entity.SysRole;
import com.mifuns.system.facade.mapper.SysRoleMapper;
import com.mifuns.system.facade.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by miguangying on 2017/3/3.
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService{

    @Resource
    SysRoleMapper sysRoleMapper;


    @Override
    public Page<SysRole> queryPageList(PageBean pageBean) {
        return sysRoleMapper.queryPageList(pageBean);
    }
}
