package com.mifuns.system.service.auth;

import com.google.common.collect.Sets;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.system.facade.entity.*;
import com.mifuns.system.facade.mapper.AuthorizationMapper;
import com.mifuns.system.facade.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/12.
 */
@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    @javax.annotation.Resource
    private AuthorizationMapper authorizationMapper;

    @javax.annotation.Resource
    private UserService userService;

    @javax.annotation.Resource
    private RoleService roleService;

    @javax.annotation.Resource
    private ResourceService resourceService;

    @javax.annotation.Resource
    private AppService appService;


    @Override
    public int createAuthorization(Long user, Long app, String roleIds) {
        Iterable<String> ids = CollectionUtil.split(roleIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        List<Authorization> saveAuths = new ArrayList<>();
        List<Authorization> all = findAll();
        for(String roleId : _ids){
            Authorization auth = new Authorization();
            auth.setUserId(user);
            auth.setAppId(app);
            auth.setRoleId(Long.valueOf(roleId));
            if(!exist(auth, all)){
                saveAuths.add(auth);
            }
        }
        if(saveAuths.isEmpty()){
            return 0;
        }
        logger.info("save : {}", saveAuths);
        return authorizationMapper.saveAuthorization(saveAuths);
    }

    protected boolean exist(Authorization authorization ,List<Authorization> all){
        for(Authorization ah : all){
            if(compareToLong(authorization.getUserId(),ah.getUserId())
                    && compareToLong(authorization.getAppId(), ah.getAppId())
                    && compareToLong(authorization.getRoleId(), ah.getRoleId())){
                return true;
            }
        }
        return false;
    }
    protected boolean compareToLong(Long a, Long b){
        return a.compareTo(b) == 0;
    }

    @Override
    public int deleteAuthorizations(List<Authorization> authorizations) {
        return 0;
    }

    @Override
    public Authorization createAuthorization(Authorization authorization) {
        return merge(authorization);
    }

    public Authorization merge(Authorization authorization) {
        Authorization dbAuthorization = authorizationMapper.findByAppUser(authorization);
        if(dbAuthorization ==  null) {//如果数据库中不存在相应记录 直接新增
            long id = authorizationMapper.insert(authorization);
            return authorization;
        }
        if(dbAuthorization.equals(authorization)) { //如果是同一条记录直接更新即可
            return authorizationMapper.updateByPrimaryKeySelective(authorization) > 0 ? authorization : null;
        }

//        for(Long roleId : authorization.getRoleIdList()) {//否则合并
//            if(!dbAuthorization.getRoleIdList().contains(roleId)) {
//                dbAuthorization.getRoleIdList().add(roleId);
//            }
//        }
//        if(dbAuthorization.getRoleIds().isEmpty()) {//如果没有角色 直接删除记录即可
//            authorizationMapper.deleteByPrimaryKey(dbAuthorization.getId());
//            return dbAuthorization;
//        }
//        dbAuthorization.setRoleIds(dbAuthorization.getRoleIdList());
        //否则更新
        return authorizationMapper.updateByPrimaryKeySelective(dbAuthorization) > 0 ? authorization : null;
    }

    @Override
    public Authorization updateAuthorization(Authorization authorization) {
        return merge(authorization);
    }

    @Override
    public Authorization updateAuthorizationRoles(Authorization authorization) {
        long row = authorizationMapper.updateByPrimaryKey(authorization);
        authorization.setId(row);
        return authorization;
    }

    @Override
    public void deleteAuthorization(Long authorizationId) {
        authorizationMapper.deleteByPrimaryKey(authorizationId);
    }

    @Override
    public Authorization findOne(Long authorizationId) {
        return authorizationMapper.selectByPrimaryKey(authorizationId);
    }

    @Override
    public List<Authorization> findAll() {
        List<Authorization> authorizations = authorizationMapper.findAll();
        Map<Long, User> users = userService.findUsers();
        Map<Long, Role> roles = roleService.findRoles();
        Map<Long, App> apps = appService.findApps();
        List<Authorization> list = new ArrayList<>();
        for(Authorization authorization : authorizations){
            authorization.setApp(apps.get(authorization.getAppId()));
            authorization.setUser(users.get(authorization.getUserId()));
            authorization.setRole(roles.get(authorization.getRoleId()));
            list.add(authorization);
        }
        return list;
    }

    @Override
    public List<Role> findRoles(String appKey, String username) {
        return roleService.findRolesByAppUser(appKey,username);
    }

    @Override
    public List<Role> findAllRoles(String username) {
        return roleService.findAllRolesByUser(username);
    }

    @Override
    public List<Resource> findResources(String appKey, String username) {
        return resourceService.findResourcesByAppUser(appKey, username);
    }

    @Override
    public Boolean isUserAtRole(String appKey, String username, String roleIds) {
        /*List<Resource> userResources = findResources(appKey,username);
        if(userResources == null) {
            return false;
        }
        List<Resource> roleResources = resourceService.findResourcesByRoleIds(roleIds);
        if(roleResources == null) {
            return false;
        }
        return PermissionUtil.hasPermission(userResources, roleResources);*/
        return true;
    }

    @Override
    public List<Resource> findMenusByAppUser(String appKey, String username) {
        List<Resource> menus = new ArrayList<>();
        return menus;
    }

    @Override
    public PermissionEntity getPermissionEntity(String appKey, String username) {
        return null;
    }
}
