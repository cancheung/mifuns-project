package com.mifuns.system.facade.mapper;

import com.mifuns.common.page.CommonPageMapper;
import com.mifuns.system.facade.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper extends CommonPageMapper<SysUser> {
    /**
     * sys_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int insert(SysUser record);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int insertSelective(SysUser record);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    SysUser selectByPrimaryKey(Long userId);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysUser record);

    SysUser validateUser(@Param("username")String username, @Param("password")String password);

    List<SysUser> querySysUserByIds(List<Long> userIds);

    List<SysUser> findAll();

    SysUser findByUsername(String username);

    /**
     * 拥有角色的用户列表
     * @param roleIds
     * @return
     */
    List<SysUser> findUserByRole(@Param("roleIds") String roleIds);
}