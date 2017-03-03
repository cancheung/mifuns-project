package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.SysResource;

public interface SysResourceMapper {
    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long resourceId);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int insert(SysResource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int insertSelective(SysResource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    SysResource selectByPrimaryKey(Long resourceId);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysResource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysResource record);
}