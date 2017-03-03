package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.SysUser;

public interface SysUserMapper {
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
}