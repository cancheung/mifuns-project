package com.mifuns.system.facade.service;

import com.mifuns.system.facade.entity.SysUser;

/**
 * Created by miguangying on 2017/3/3.
 */
public interface SysUserService {
    SysUser queryUserByPassword(String username,String password);
}
