package com.mifuns.system.service.sysrole;

import com.github.pagehelper.Page;
import com.google.common.collect.Sets;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.Role;
import com.mifuns.system.facade.enums.ResourceStatus;
import com.mifuns.system.facade.mapper.RoleMapper;
import com.mifuns.system.facade.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by miguangying on 2017/3/3.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService{

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @javax.annotation.Resource
    RoleMapper roleMapper;


    @Override
    public Page<Role> queryPageList(PageBean pageBean) {
        return roleMapper.queryPageList(pageBean);
    }


    @Override
    public Role createRole(Role role) {
        roleMapper.insert(role);
        return role;
    }

    @Override
    public Role updateRole(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role) > 0 ? role : null;
    }

    @Override
    public int deleteRole(Long roleId) {
        return roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int deleteRoles(String roleIds) {
        Iterable<String> ids = CollectionUtil.split(roleIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String roleId : _ids) {
            i += deleteRole(Long.valueOf(roleId));
        }
        return i;
    }

    @Override
    public int disableRoles(String roleIds) {
        Iterable<String> ids = CollectionUtil.split(roleIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String roleId : _ids) {
            if(null != updateRole(new Role(Long.valueOf(roleId), ResourceStatus.UNAVAILABLE.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public Role findOne(Long roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    public List<Role> findRoleList(Long... roleIds) {
        String roleIdsStr = CollectionUtil.arrayLongConvertStr(roleIds);
        return findRoleList(roleIdsStr);
    }

    @Override
    public List<Role> findRoleList(String roleIdsStr) {
        return roleMapper.findRoles(roleIdsStr);
    }

    @Override
    public Set<String> findRoleNames(Long... roleIds) {
        Set<String> roles = new HashSet<String>();
        List<Role> roleList = findRoleList(roleIds);
        for(Role role : roleList){
            if(role != null) {
                roles.add(role.getRoleName());
            }
        }
        return roles;
    }

    @Override
    public List<Role> findRolesByAppUser(String appKey, String username) {
        Map<String,String> map = new HashMap<>();
        map.put("appKey", appKey);
        map.put("username", username);
        return roleMapper.findRolesByAppUser(map);
    }

    @Override
    public List<Role> findAllRolesByUser(String username) {
        Map<String,String> map = new HashMap<>();
        map.put("username", username);
        return roleMapper.findRolesByAppUser(map);
    }

    @Override
    public Map<Long, Role> findRoles() {
        Map<Long, Role> map = new HashMap<>();
        List<Role> list = findAll();
        for(Role role : list){
            map.put(role.getRoleId(), role);
        }
        return map;
    }

    @Override
    public void clearCache() {
        logger.info("clear role cache.");
    }
}
