package com.mifuns.system.facade.service;

import com.mifuns.common.service.BaseCommonService;
import com.mifuns.system.facade.entity.Authorization;
import com.mifuns.system.facade.entity.PermissionEntity;
import com.mifuns.system.facade.entity.Resource;
import com.mifuns.system.facade.entity.Role;

import java.util.List;

/**
 * Created by miguangying on 2017/3/12.
 */
public interface AuthorizationService {
    /**
     * 去重添加
     * @param user
     * @param app
     * @param roleIds
     * @return
     */
    int createAuthorization(Long user, Long app, String roleIds);


    /**
     * 批量删除授权
     * @param authorizations
     */
    int deleteAuthorizations(List<Authorization> authorizations);

    /**
     * user_id,app_id 存在 合并，否则只添加
     * @param authorization
     * @return
     */
    public Authorization createAuthorization(Authorization authorization);

    /**
     * user_id,app_id 存在 合并，否则只添加
     * @param authorization
     * @return
     */
    public Authorization updateAuthorization(Authorization authorization);

    /**
     * 更新role_ids
     * @param authorization
     * @return
     */
    public Authorization updateAuthorizationRoles(Authorization authorization);
    public void deleteAuthorization(Long authorizationId);

    public Authorization findOne(Long authorizationId);
    public List<Authorization> findAll();

    /**
     * 获取APP和用户的角色列表
     * @param appKey
     * @param username
     * @return
     */
    public List<Role> findRoles(String appKey, String username);

    /**
     * 获取用户的角色列表
     * @param username
     * @return
     */
    public List<Role> findAllRoles(String username);


    /**
     * 根据AppKey和用户名查找资源
     * @param appKey
     * @param username
     * @return
     */
    public List<Resource> findResources(String appKey, String username);

    /**
     * 判断用户是否拥有权限
     * @param appKey
     * @param username
     * @param roleIds
     * @return
     */
    public Boolean isUserAtRole(String appKey, String username,String roleIds);


    /***
     * 获取用户应用菜单
     * 支持动态地址，全局变量  ${support_url}
     * @param appKey
     * @param username
     * @return
     */
    List<Resource> findMenusByAppUser(String appKey, String username);

    PermissionEntity getPermissionEntity(String appKey, String username);
}
