package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.RoleResource;

public interface RoleResourceMapper {
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
    int insert(RoleResource record);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int insertSelective(RoleResource record);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    RoleResource selectByPrimaryKey(Long id);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(RoleResource record);

    /**
     * sys_role_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(RoleResource record);
}