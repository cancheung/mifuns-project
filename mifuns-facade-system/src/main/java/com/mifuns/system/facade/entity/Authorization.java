package com.mifuns.system.facade.entity;

import java.util.Date;

public class Authorization {
    /**
     *
     * sys_user_app_role.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 用户ID
     * sys_user_app_role.user_id
     *
     * @mbggenerated
     */
    private Long userId;
    private User user;

    /**
     * 应用ID
     * sys_user_app_role.app_id
     *
     * @mbggenerated
     */
    private Long appId;
    private App app;

    /**
     * 角色ID
     * sys_user_app_role.role_id
     *
     * @mbggenerated
     */
    private Long roleId;
    private Role role;

    /**
     * 插入时间
     * sys_user_app_role.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_user_app_role.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     *
     * sys_user_app_role.id
     *
     * @return sys_user_app_role.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * sys_user_app_role.id
     *
     * @param id sys_user_app_role.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户ID
     * sys_user_app_role.user_id
     *
     * @return sys_user_app_role.user_id
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     * sys_user_app_role.user_id
     *
     * @param userId sys_user_app_role.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 应用ID
     * sys_user_app_role.app_id
     *
     * @return sys_user_app_role.app_id
     *
     * @mbggenerated
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * 应用ID
     * sys_user_app_role.app_id
     *
     * @param appId sys_user_app_role.app_id
     *
     * @mbggenerated
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * 角色ID
     * sys_user_app_role.role_id
     *
     * @return sys_user_app_role.role_id
     *
     * @mbggenerated
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 角色ID
     * sys_user_app_role.role_id
     *
     * @param roleId sys_user_app_role.role_id
     *
     * @mbggenerated
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 插入时间
     * sys_user_app_role.insert_date
     *
     * @return sys_user_app_role.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_user_app_role.insert_date
     *
     * @param insertDate sys_user_app_role.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_user_app_role.update_date
     *
     * @return sys_user_app_role.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_user_app_role.update_date
     *
     * @param updateDate sys_user_app_role.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}