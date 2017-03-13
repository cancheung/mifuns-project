package com.mifuns.system.service.sysres;

import com.github.pagehelper.Page;
import com.google.common.collect.Sets;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.Resource;
import com.mifuns.system.facade.enums.ResourceStatus;
import com.mifuns.system.facade.mapper.ResourceMapper;
import com.mifuns.system.facade.mapper.RoleResourceMapper;
import com.mifuns.system.facade.service.ResourceService;
import com.mifuns.system.service.util.PermissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by miguangying on 2017/3/9.
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @javax.annotation.Resource
    ResourceMapper resourceMapper;

    @javax.annotation.Resource
    RoleResourceMapper roleResourceMapper;

    @Override
    public Page queryPageList(PageBean pageBean) {
        return resourceMapper.queryPageList(pageBean);
    }

    @Override
    public Resource createResource(Resource resource) {
        resourceMapper.insert(resource);
        return resource;
    }

    @Override
    public Resource updateResource(Resource resource) {
        return resourceMapper.updateByPrimaryKeySelective(resource) > 0 ? resource : null;
    }

    @Override
    public int deleteResource(Long resourceId) {
        return resourceMapper.deleteByPrimaryKey(resourceId);
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
            if(null != updateResource(new Resource(Long.valueOf(resourceId), ResourceStatus.UNAVAILABLE.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public Resource findOne(Long resourceId) {
        return resourceMapper.selectByPrimaryKey(resourceId);
    }

    @Override
    public List<Resource> findAll() {
        return resourceMapper.findAll();
    }

    @Override
    public List<Resource> findResources(String ids) {
        return resourceMapper.findResources(ids);
    }

    @Override
    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        List<Resource> list = findResources(CollectionUtil.arrayLongConvertStr(resourceIds));
        for(Resource resource : list){
            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }

    @Override
    public List<Resource> findMenus(Set<String> resourceIds) {
        List<Resource> allResources = findMenuAll();
        return findMenus(allResources,resourceIds);
    }

    @Override
    public List<Resource> findMenuAll() {
        return null;
    }

    @Override
    public List<Resource> findMenus(List<Resource> allResources, Set<String> permissions) {
        List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : allResources) {
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
    public List<Resource> findResourcesByAppUser(String appKey, String username) {
        return null;
    }

    @Override
    public List<Resource> findResourcesByRoleIds(String roleIds) {
        return null;
    }

    @Override
    public List<Resource> findMenus(List<Resource> permissionsResources) {
        return null;
    }

    @Override
    public int updateRoleResources(Long id, String resourceIds) {
        return 0;
    }

    @Override
    public Map<Long, Resource> findResources() {
        return null;
    }

    @Override
    public void clearCache() {

    }

    private boolean hasPermission(Set<String> permissions, Resource resource) {
        return PermissionUtil.hasPermission(permissions,resource);
    }
}
