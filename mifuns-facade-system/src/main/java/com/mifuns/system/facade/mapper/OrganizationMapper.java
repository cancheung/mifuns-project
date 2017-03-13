package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.Organization;

public interface OrganizationMapper {
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
    int insert(Organization record);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int insertSelective(Organization record);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    Organization selectByPrimaryKey(Long organizationId);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Organization record);

    /**
     * sys_organization
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Organization record);
}