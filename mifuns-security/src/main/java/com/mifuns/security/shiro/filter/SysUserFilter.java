package com.mifuns.security.shiro.filter;

import com.mifuns.security.shiro.constants.SecurityConstants;
import com.mifuns.system.facade.entity.User;
import com.mifuns.system.facade.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/4/26 </p>
 * <p>Time: 16:57 </p>
 * <p>Version: 1.0 </p>
 */
public class SysUserFilter extends PathMatchingFilter {

    @javax.annotation.Resource
    private UserService userService;
    public static final String ATTR_CURRENT_USER = "attrCurrentUser";


    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        Object obj = subject.getSession(true).getAttribute(ATTR_CURRENT_USER);
        if(obj != null){
            if(obj instanceof User){
                request.setAttribute(SecurityConstants.CURRENT_USER, obj);
                return true;
            }
        }
        User user = userService.findByUsername(username);
        subject.getSession(true).setAttribute(ATTR_CURRENT_USER, user);
        request.setAttribute(SecurityConstants.CURRENT_USER, user);
//        AuthenticationListener
        return true;
    }
}
