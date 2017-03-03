package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.SysOrganization;

public interface SysOrganizationMapper {
    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long organizationId);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int insert(SysOrganization record);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int insertSelective(SysOrganization record);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    SysOrganization selectByPrimaryKey(Long organizationId);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysOrganization record);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysOrganization record);
}