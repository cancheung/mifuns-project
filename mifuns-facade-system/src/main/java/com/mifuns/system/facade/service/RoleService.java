package com.mifuns.system.facade.service;

import com.mifuns.common.service.BaseCommonService;
import com.mifuns.system.facade.entity.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguangying on 2017/3/3.
 */
public interface RoleService extends BaseCommonService {
    public Role createRole(Role role);
    public Role updateRole(Role role);
    public int deleteRole(Long roleId);

    /**
     * 批量删除角色
     * @param roleIds
     */
    int deleteRoles(String roleIds);

    /**
     * 批量禁用角色
     * @param roleIds
     */
    int disableRoles(String roleIds);

    public Role findOne(Long roleId);
    public List<Role> findAll();

    /**
     * 根据角色编号得到角色标识符列表
     * @param roleIds
     * @return
     */
    public List<Role> findRoleList(Long... roleIds);
    public List<Role> findRoleList(String roleIdsStr);

    /**
     * 角色名称集合
     * @param roleIds
     * @return
     */
    Set<String> findRoleNames(Long... roleIds);

    List<Role> findRolesByAppUser(String appKey, String username);
    List<Role> findAllRolesByUser(String username);

    /**
     * id=role
     * @return
     */
    Map<Long, Role> findRoles();

    /**
     * 清理角色缓存
     */
    void clearCache();
}
