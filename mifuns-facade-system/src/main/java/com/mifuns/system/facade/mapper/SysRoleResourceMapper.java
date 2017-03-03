package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.SysRoleResource;

public interface SysRoleResourceMapper {
    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int insert(SysRoleResource record);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int insertSelective(SysRoleResource record);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    SysRoleResource selectByPrimaryKey(Long id);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysRoleResource record);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysRoleResource record);
}