package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.SysRole;

public interface SysRoleMapper {
    /**
     * sys_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long roleId);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int insert(SysRole record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int insertSelective(SysRole record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    SysRole selectByPrimaryKey(Long roleId);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysRole record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysRole record);
}