package com.mifuns.system.facade.service;

import com.github.pagehelper.Page;
import com.mifuns.common.service.BaseCommonService;
import com.mifuns.system.facade.entity.SysRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguangying on 2017/3/3.
 */
public interface SysRoleService extends BaseCommonService {
    public SysRole createSysRole(SysRole sysRole);
    public SysRole updateSysRole(SysRole sysRole);
    public int deleteSysRole(Long roleId);

    /**
     * 批量删除角色
     * @param roleIds
     */
    int deleteSysRoles(String roleIds);

    /**
     * 批量禁用角色
     * @param roleIds
     */
    int disableSysRoles(String roleIds);

    public SysRole findOne(Long roleId);
    public List<SysRole> findAll();

    /**
     * 根据角色编号得到角色标识符列表
     * @param roleIds
     * @return
     */
    public List<SysRole> findSysRoleList(Long... roleIds);
    public List<SysRole> findSysRoleList(String roleIdsStr);

    /**
     * 角色名称集合
     * @param roleIds
     * @return
     */
    Set<String> findSysRoleNames(Long... roleIds);

    List<SysRole> findSysRolesByAppUser(String appKey, String username);
    List<SysRole> findAllSysRolesByUser(String username);

    /**
     * id=role
     * @return
     */
    Map<Long, SysRole> findSysRoles();

    /**
     * 清理角色缓存
     */
    void clearCache();
}
