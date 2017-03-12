package com.mifuns.system.service.sysrole;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.SysRole;
import com.mifuns.system.facade.enums.ResourceStatus;
import com.mifuns.system.facade.mapper.SysRoleMapper;
import com.mifuns.system.facade.service.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by miguangying on 2017/3/3.
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService{

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Resource
    SysRoleMapper sysRoleMapper;


    @Override
    public Page<SysRole> queryPageList(PageBean pageBean) {
        return sysRoleMapper.queryPageList(pageBean);
    }


    @Override
    public SysRole createSysRole(SysRole sysRole) {
        sysRoleMapper.insert(sysRole);
        return sysRole;
    }

    @Override
    public SysRole updateSysRole(SysRole sysRole) {
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole) > 0 ? sysRole : null;
    }

    @Override
    public int deleteSysRole(Long roleId) {
        return sysRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int deleteSysRoles(String roleIds) {
        Iterable<String> ids = CollectionUtil.split(roleIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String roleId : _ids) {
            i += deleteSysRole(Long.valueOf(roleId));
        }
        return i;
    }

    @Override
    public int disableSysRoles(String roleIds) {
        Iterable<String> ids = CollectionUtil.split(roleIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String roleId : _ids) {
            if(null != updateSysRole(new SysRole(Long.valueOf(roleId), ResourceStatus.UNAVAILABLE.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public SysRole findOne(Long roleId) {
        return sysRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public List<SysRole> findAll() {
        return sysRoleMapper.findAll();
    }

    @Override
    public List<SysRole> findSysRoleList(Long... roleIds) {
        String roleIdsStr = CollectionUtil.arrayLongConvertStr(roleIds);
        return findSysRoleList(roleIdsStr);
    }

    @Override
    public List<SysRole> findSysRoleList(String roleIdsStr) {
        return sysRoleMapper.findRoles(roleIdsStr);
    }

    @Override
    public Set<String> findSysRoleNames(Long... roleIds) {
        Set<String> roles = new HashSet<String>();
        List<SysRole> roleList = findSysRoleList(roleIds);
        for(SysRole role : roleList){
            if(role != null) {
                roles.add(role.getRoleName());
            }
        }
        return roles;
    }

    @Override
    public List<SysRole> findSysRolesByAppUser(String appKey, String username) {
        Map<String,String> map = new HashMap<>();
        map.put("appKey", appKey);
        map.put("username", username);
        return sysRoleMapper.findRolesByAppUser(map);
    }

    @Override
    public List<SysRole> findAllSysRolesByUser(String username) {
        Map<String,String> map = new HashMap<>();
        map.put("username", username);
        return sysRoleMapper.findRolesByAppUser(map);
    }

    @Override
    public Map<Long, SysRole> findSysRoles() {
        Map<Long, SysRole> map = new HashMap<>();
        List<SysRole> list = findAll();
        for(SysRole role : list){
            map.put(role.getRoleId(), role);
        }
        return map;
    }

    @Override
    public void clearCache() {
        logger.info("clear role cache.");
    }
}
