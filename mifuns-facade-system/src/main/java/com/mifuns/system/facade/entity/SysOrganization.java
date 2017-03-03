package com.mifuns.system.facade.entity;

import com.mifuns.common.page.PageBean;

import java.util.Date;

public class SysOrganization extends PageBean {
    /**
     *
     * sys_organization.organization_id
     *
     * @mbggenerated
     */
    private Long organizationId;

    /**
     * 组织名称
     * sys_organization.organization_name
     *
     * @mbggenerated
     */
    private String organizationName;

    /**
     * 父ID,顶级节点默认0
     * sys_organization.parent_id
     *
     * @mbggenerated
     */
    private Long parentId;

    /**
     *
     * sys_organization.parent_ids
     *
     * @mbggenerated
     */
    private String parentIds;

    /**
     * 1 有效
     * sys_organization.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * logo icon图标
     * sys_organization.logo
     *
     * @mbggenerated
     */
    private String logo;

    /**
     * 插入时间
     * sys_organization.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_organization.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     *
     * sys_organization.organization_id
     *
     * @return sys_organization.organization_id
     *
     * @mbggenerated
     */
    public Long getOrganizationId() {
        return organizationId;
    }

    /**
     *
     * sys_organization.organization_id
     *
     * @param organizationId sys_organization.organization_id
     *
     * @mbggenerated
     */
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 组织名称
     * sys_organization.organization_name
     *
     * @return sys_organization.organization_name
     *
     * @mbggenerated
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * 组织名称
     * sys_organization.organization_name
     *
     * @param organizationName sys_organization.organization_name
     *
     * @mbggenerated
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    /**
     * 父ID,顶级节点默认0
     * sys_organization.parent_id
     *
     * @return sys_organization.parent_id
     *
     * @mbggenerated
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 父ID,顶级节点默认0
     * sys_organization.parent_id
     *
     * @param parentId sys_organization.parent_id
     *
     * @mbggenerated
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     *
     * sys_organization.parent_ids
     *
     * @return sys_organization.parent_ids
     *
     * @mbggenerated
     */
    public String getParentIds() {
        return parentIds;
    }

    /**
     *
     * sys_organization.parent_ids
     *
     * @param parentIds sys_organization.parent_ids
     *
     * @mbggenerated
     */
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds == null ? null : parentIds.trim();
    }

    /**
     * 1 有效
     * sys_organization.status
     *
     * @return sys_organization.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1 有效
     * sys_organization.status
     *
     * @param status sys_organization.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * logo icon图标
     * sys_organization.logo
     *
     * @return sys_organization.logo
     *
     * @mbggenerated
     */
    public String getLogo() {
        return logo;
    }

    /**
     * logo icon图标
     * sys_organization.logo
     *
     * @param logo sys_organization.logo
     *
     * @mbggenerated
     */
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    /**
     * 插入时间
     * sys_organization.insert_date
     *
     * @return sys_organization.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_organization.insert_date
     *
     * @param insertDate sys_organization.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_organization.update_date
     *
     * @return sys_organization.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_organization.update_date
     *
     * @param updateDate sys_organization.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}