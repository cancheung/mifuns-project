package com.mifuns.security.shiro.realm;

import com.mifuns.system.facade.entity.SysUser;
import com.mifuns.system.facade.service.AuthorizationService;
import com.mifuns.system.facade.service.SysUserService;
import com.mifuns.system.service.util.PermissionUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miguangying on 2017/3/12.
 */
public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @javax.annotation.Resource
    private SysUserService sysUserService;
    @javax.annotation.Resource
    private AuthorizationService authorizationService;

    private String appKey;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();
        logger.info("appKey = {} ,username = {}",appKey, username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(PermissionUtil.getRoleNameByRoles(authorizationService.findRoles(appKey, username)));
        authorizationInfo.setStringPermissions(PermissionUtil.getPermissionsByResources(authorizationService.findResources(appKey, username)));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();
        logger.info("username = {}", username);
        SysUser sysUser = sysUserService.findByUsername(username);
        if(sysUser == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        if(sysUser.isLock()) {
            throw new LockedAccountException(); //帐号锁定
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                sysUser.getUsername(), //用户名
                sysUser.getPassword(), //密码
                ByteSource.Util.bytes(sysUser.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }


    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
