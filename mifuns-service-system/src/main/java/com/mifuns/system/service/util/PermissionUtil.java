package com.mifuns.system.service.util;

import com.mifuns.system.facade.entity.Resource;
import com.mifuns.system.facade.entity.Role;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/4/29 </p>
 * <p>Time: 11:22 </p>
 * <p>Version: 1.0 </p>
 */
public abstract class PermissionUtil {

    /**
     * 权限包含资源
     * @param permissions
     * @param resource
     * @return
     */
    public static boolean hasPermission(Set<String> permissions, Resource resource) {
        if(StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }
        for(String permission : permissions) {
            WildcardPermission p1 = getWildcardPermission(permission);
            WildcardPermission p2 = getWildcardPermission(resource.getPermission());
            if(p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param useResources 拥有的资源
     * @param checkResources 包含的资源
     * @return
     */
    public static boolean hasPermission(List<Resource> useResources, List<Resource> checkResources){
        for (Resource resource : useResources){
            if(hasPermission(resource, checkResources)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param useResource 拥有的资源
     * @param checkResources 包含的资源
     * @return
     */
    public static boolean hasPermission(Resource useResource, List<Resource> checkResources){
        WildcardPermission p1 = getWildcardPermission(useResource.getPermission());
        for(Resource checkResource : checkResources){
            WildcardPermission p2 = getWildcardPermission(checkResource.getPermission());
            if(p1.implies(p2)){
                return true;
            }
        }
        return false;
    }
    private static WildcardPermission getWildcardPermission(String permission){
        return new WildcardPermission(permission);
    }

    public static Set<String> getPermissionsByResources(List<Resource> resourceList){
        Set<String> permissions = new HashSet<>();
        for(Resource resource : resourceList){
            permissions.add(resource.getPermission());
        }
        return permissions;
    }

    public static Set<String> getRoleNameByRoles(List<Role> roleList){
        Set<String> roles = new HashSet<>();
        for(Role role : roleList){
            roles.add(role.getRoleName());
        }
        return roles;
    }
}
