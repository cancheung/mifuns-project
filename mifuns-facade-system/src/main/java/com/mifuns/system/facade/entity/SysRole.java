package com.mifuns.system.facade.entity;

import com.mifuns.common.page.PageBean;

import java.util.Date;

public class SysRole extends PageBean {
    /**
     *
     * sys_role.role_id
     *
     * @mbggenerated
     */
    private Long roleId;

    /**
     * 角色
     * sys_role.role_name
     *
     * @mbggenerated
     */
    private String roleName;

    /**
     * 角色描述
     * sys_role.description
     *
     * @mbggenerated
     */
    private String description;

    /**
     * 1 有效
     * sys_role.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * 插入时间
     * sys_role.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_role.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     *
     * sys_role.role_id
     *
     * @return sys_role.role_id
     *
     * @mbggenerated
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     *
     * sys_role.role_id
     *
     * @param roleId sys_role.role_id
     *
     * @mbggenerated
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 角色
     * sys_role.role_name
     *
     * @return sys_role.role_name
     *
     * @mbggenerated
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 角色
     * sys_role.role_name
     *
     * @param roleName sys_role.role_name
     *
     * @mbggenerated
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 角色描述
     * sys_role.description
     *
     * @return sys_role.description
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * 角色描述
     * sys_role.description
     *
     * @param description sys_role.description
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 1 有效
     * sys_role.status
     *
     * @return sys_role.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1 有效
     * sys_role.status
     *
     * @param status sys_role.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 插入时间
     * sys_role.insert_date
     *
     * @return sys_role.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_role.insert_date
     *
     * @param insertDate sys_role.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_role.update_date
     *
     * @return sys_role.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_role.update_date
     *
     * @param updateDate sys_role.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", insertDate=" + insertDate +
                ", updateDate=" + updateDate +
                '}';
    }
}