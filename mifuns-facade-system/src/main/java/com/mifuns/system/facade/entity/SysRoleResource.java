package com.mifuns.system.facade.entity;

import java.util.Date;

public class SysRoleResource {
    /**
     *
     * sys_role_resource.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 角色ID
     * sys_role_resource.role_id
     *
     * @mbggenerated
     */
    private Long roleId;

    /**
     * 资源ID
     * sys_role_resource.resource_id
     *
     * @mbggenerated
     */
    private Long resourceId;

    /**
     * 是否有效 1 有效
     * sys_role_resource.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * 插入时间
     * sys_role_resource.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_role_resource.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     *
     * sys_role_resource.id
     *
     * @return sys_role_resource.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * sys_role_resource.id
     *
     * @param id sys_role_resource.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 角色ID
     * sys_role_resource.role_id
     *
     * @return sys_role_resource.role_id
     *
     * @mbggenerated
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 角色ID
     * sys_role_resource.role_id
     *
     * @param roleId sys_role_resource.role_id
     *
     * @mbggenerated
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 资源ID
     * sys_role_resource.resource_id
     *
     * @return sys_role_resource.resource_id
     *
     * @mbggenerated
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * 资源ID
     * sys_role_resource.resource_id
     *
     * @param resourceId sys_role_resource.resource_id
     *
     * @mbggenerated
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 是否有效 1 有效
     * sys_role_resource.status
     *
     * @return sys_role_resource.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 是否有效 1 有效
     * sys_role_resource.status
     *
     * @param status sys_role_resource.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 插入时间
     * sys_role_resource.insert_date
     *
     * @return sys_role_resource.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_role_resource.insert_date
     *
     * @param insertDate sys_role_resource.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_role_resource.update_date
     *
     * @return sys_role_resource.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_role_resource.update_date
     *
     * @param updateDate sys_role_resource.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}