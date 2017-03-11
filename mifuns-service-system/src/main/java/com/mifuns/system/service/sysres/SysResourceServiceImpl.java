package com.mifuns.system.service.sysres;

import com.github.pagehelper.Page;
import com.mifuns.common.page.PageBean;
import com.mifuns.system.facade.entity.SysResource;
import com.mifuns.system.facade.mapper.SysResourceMapper;
import com.mifuns.system.facade.service.SysResourceService;

import javax.annotation.Resource;

/**
 * Created by miguangying on 2017/3/9.
 */
public class SysResourceServiceImpl implements SysResourceService {
    @Resource
    SysResourceMapper sysResourceMapper;
    @Override
    public Page queryPageList(PageBean pageBean) {
        return sysResourceMapper.queryPageList(pageBean);
    }
}
