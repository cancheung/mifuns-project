package com.mifuns.system.service.sysres;

import com.github.pagehelper.Page;
import com.google.common.collect.Sets;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.SysResource;
import com.mifuns.system.facade.enums.ResourceStatus;
import com.mifuns.system.facade.enums.ResourceType;
import com.mifuns.system.facade.mapper.SysResourceMapper;
import com.mifuns.system.facade.mapper.SysRoleResourceMapper;
import com.mifuns.system.facade.service.SysResourceService;
import com.mifuns.system.service.util.PermissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by miguangying on 2017/3/9.
 */
@Service("sysResourceService")
public class SysResourceServiceImpl implements SysResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SysResourceServiceImpl.class);

    @Resource
    SysResourceMapper sysResourceMapper;

    @javax.annotation.Resource
    SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    public Page queryPageList(PageBean pageBean) {
        return sysResourceMapper.queryPageList(pageBean);
    }

    @Override
    public SysResource createResource(SysResource resource) {
        sysResourceMapper.insert(resource);
        return resource;
    }

    @Override
    public SysResource updateResource(SysResource resource) {
        return sysResourceMapper.updateByPrimaryKeySelective(resource) > 0 ? resource : null;
    }

    @Override
    public int deleteResource(Long resourceId) {
        return sysResourceMapper.deleteByPrimaryKey(resourceId);
    }

    @Override
    public int deleteResources(String resourceIds) {
        Iterable<String> ids = CollectionUtil.split(resourceIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String resourceId : _ids) {
            i += deleteResource(Long.valueOf(resourceId));
        }
        return i;
    }

    @Override
    public int disableResources(String resourceIds) {
        Iterable<String> ids = CollectionUtil.split(resourceIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String resourceId : _ids) {
            if(null != updateResource(new SysResource(Long.valueOf(resourceId), ResourceStatus.UNAVAILABLE.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public SysResource findOne(Long resourceId) {
        return sysResourceMapper.selectByPrimaryKey(resourceId);
    }

    @Override
    public List<SysResource> findAll() {
        return sysResourceMapper.findAll();
    }

    @Override
    public List<SysResource> findResources(String ids) {
        return sysResourceMapper.findResources(ids);
    }

    @Override
    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        List<SysResource> list = findResources(CollectionUtil.arrayLongConvertStr(resourceIds));
        for(SysResource resource : list){
            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }

    @Override
    public List<SysResource> findMenus(Set<String> resourceIds) {
        List<SysResource> allResources = findMenuAll();
        return findMenus(allResources,resourceIds);
    }

    @Override
    public List<SysResource> findMenuAll() {
        return null;
    }

    @Override
    public List<SysResource> findMenus(List<SysResource> allResources, Set<String> permissions) {
        List<SysResource> menus = new ArrayList<SysResource>();
        for(SysResource resource : allResources) {
//            if(resource.isRootNode()) {
//                continue;
//            }
            if(!resource.isAvailabled()){
                continue;
            }
            if(resource.isButton()) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }

    @Override
    public List<SysResource> findResourcesByAppUser(String appKey, String username) {
        return null;
    }

    @Override
    public List<SysResource> findResourcesByRoleIds(String roleIds) {
        return null;
    }

    @Override
    public List<SysResource> findMenus(List<SysResource> permissionsResources) {
        return null;
    }

    @Override
    public int updateRoleResources(Long id, String resourceIds) {
        return 0;
    }

    @Override
    public Map<Long, SysResource> findResources() {
        return null;
    }

    @Override
    public void clearCache() {

    }

    private boolean hasPermission(Set<String> permissions, SysResource resource) {
        return PermissionUtil.hasPermission(permissions,resource);
    }
}
