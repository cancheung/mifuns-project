package com.mifuns.system.facade.service;


import com.mifuns.common.service.BaseCommonService;
import com.mifuns.system.facade.entity.SysResource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguangying on 2017/3/3.
 */
public interface SysResourceService extends BaseCommonService {
    public SysResource createResource(SysResource resource);
    public SysResource updateResource(SysResource resource);
    public int deleteResource(Long resourceId);

    /**
     * 批量删除资源
     * @param resourceIds
     */
    int deleteResources(String resourceIds);

    /**
     * 批量禁用资源
     * @param resourceIds
     */
    int disableResources(String resourceIds);

    SysResource findOne(Long resourceId);
    List<SysResource> findAll();
    public List<SysResource> findResources(String ids);

    /**
     * 得到资源对应的权限字符串
     * @param resourceIds
     * @return
     */
    Set<String> findPermissions(Set<Long> resourceIds);


    /**
     * 根据用户权限得到菜单
     * @param permissions
     * @return
     */
    List<SysResource> findMenus(Set<String> permissions);

    public List<SysResource> findMenuAll();
    /**
     * 根据用户权限得到菜单
     * @param allResources 全部资源
     * @param permissions
     * @return
     */
    public List<SysResource> findMenus(List<SysResource> allResources, Set<String> permissions);

    /**
     * 查询APP用户资源
     * @param appKey
     * @param username
     * @return
     */
    List<SysResource> findResourcesByAppUser(String appKey, String username);

    /**
     * 查询角色资源
     * @param roleIds
     * @return
     */
    List<SysResource> findResourcesByRoleIds(String roleIds);

    /**
     *
     * @param permissionsResources
     * @return
     */
    List<SysResource> findMenus(List<SysResource> permissionsResources);

    int updateRoleResources(Long id, String resourceIds);

    /**
     *
     * @return
     */
    Map<Long, SysResource> findResources();

    /**
     * 清理资源缓存
     */
    void clearCache();
}
