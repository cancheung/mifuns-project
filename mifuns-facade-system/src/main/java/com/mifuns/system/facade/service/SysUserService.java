package com.mifuns.system.facade.service;

import com.mifuns.common.service.BaseCommonService;
import com.mifuns.system.facade.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/3.
 */
public interface SysUserService extends BaseCommonService {
    SysUser queryUserByPassword(String username,String password);
    /**
     * 创建用户
     * @param user
     */
    public SysUser createSysUser(SysUser sysUser);

    public SysUser updateSysUser(SysUser sysUser);

    /**
     * 锁定账户，删除
     * @param userId
     */
    int deleteSysUser(Long userId);

    /**
     * 批量禁用用户
     * @param userIds
     */
    int disableSysUsers(String userIds);
    /**
     * 完全清除用户
     * @param userId
     */
    int cleanSysUser(Long userId);

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword);

    /**
     * 查询一个用户，包含锁定
     * @param userId
     * @return
     */
    SysUser findOne(Long userId);

    /**
     * 查询全部用户，包含锁定
     * @return
     */
    List<SysUser> findAll();

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public SysUser findByUsername(String username);

    /**
     * 返回用户ID，User 键值对
     * @return
     */
    Map<Long, SysUser> findSysUsers();

    /**
     * 拥有角色的用户列表
     * @param roleIds 角色ID，多个逗号隔开
     * @return
     */
    List<SysUser> findUserByRole(String roleIds);
}
