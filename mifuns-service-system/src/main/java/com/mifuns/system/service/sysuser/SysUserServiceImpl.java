package com.mifuns.system.service.sysuser;

import com.github.pagehelper.Page;
import com.mifuns.common.page.PageBean;
import com.mifuns.system.facade.entity.SysRole;
import com.mifuns.system.facade.entity.SysUser;
import com.mifuns.system.facade.mapper.SysRoleMapper;
import com.mifuns.system.facade.mapper.SysUserMapper;
import com.mifuns.system.facade.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by miguangying on 2017/3/3.
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Resource
    SysUserMapper sysUserMapper;


    @Override
    public Page<SysUser> queryPageList(PageBean pageBean) {
        return sysUserMapper.queryPageList(pageBean);
    }
}
