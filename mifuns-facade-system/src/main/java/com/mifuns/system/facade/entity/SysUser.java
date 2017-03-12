package com.mifuns.system.facade.entity;

import com.mifuns.common.page.PageBean;

import java.util.Date;

public class SysUser extends PageBean {
    /**
     *
     * sys_user.user_id
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     *
     * sys_user.organization_id
     *
     * @mbggenerated
     */
    private Long organizationId;

    /**
     * 用户名称
     * sys_user.username
     *
     * @mbggenerated
     */
    private String username;

    /**
     * 密码
     * sys_user.password
     *
     * @mbggenerated
     */
    private String password;

    /**
     * 盐
     * sys_user.salt
     *
     * @mbggenerated
     */
    private String salt;

    /**
     * 1 锁定
     * sys_user.locked
     *
     * @mbggenerated
     */
    private Integer locked;

    /**
     * 手机号
     * sys_user.phone
     *
     * @mbggenerated
     */
    private String phone;

    /**
     * 邮箱
     * sys_user.email
     *
     * @mbggenerated
     */
    private String email;

    /**
     * 头像
     * sys_user.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * 插入时间
     * sys_user.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_user.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    public SysUser(){

    }

    public SysUser(Long userId, Integer locked, Date updateDate) {
        this.userId= userId;
        this.locked = locked;
        this.updateDate = updateDate;
    }


    /**
     * 是否锁定
     * @return
     */
    public boolean isLock() {
        return this.locked == 1;
    }

    /**
     * 用户名 加盐
     * @return
     */
    public String getCredentialsSalt() {
        return username + salt;
    }

    /**
     *
     * sys_user.user_id
     *
     * @return sys_user.user_id
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *
     * sys_user.user_id
     *
     * @param userId sys_user.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *
     * sys_user.organization_id
     *
     * @return sys_user.organization_id
     *
     * @mbggenerated
     */
    public Long getOrganizationId() {
        return organizationId;
    }

    /**
     *
     * sys_user.organization_id
     *
     * @param organizationId sys_user.organization_id
     *
     * @mbggenerated
     */
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 用户名称
     * sys_user.username
     *
     * @return sys_user.username
     *
     * @mbggenerated
     */
    public String getUsername() {
        return username;
    }

    /**
     * 用户名称
     * sys_user.username
     *
     * @param username sys_user.username
     *
     * @mbggenerated
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 密码
     * sys_user.password
     *
     * @return sys_user.password
     *
     * @mbggenerated
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * sys_user.password
     *
     * @param password sys_user.password
     *
     * @mbggenerated
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 盐
     * sys_user.salt
     *
     * @return sys_user.salt
     *
     * @mbggenerated
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 盐
     * sys_user.salt
     *
     * @param salt sys_user.salt
     *
     * @mbggenerated
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * 1 锁定
     * sys_user.locked
     *
     * @return sys_user.locked
     *
     * @mbggenerated
     */
    public Integer getLocked() {
        return locked;
    }

    /**
     * 1 锁定
     * sys_user.locked
     *
     * @param locked sys_user.locked
     *
     * @mbggenerated
     */
    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    /**
     * 手机号
     * sys_user.phone
     *
     * @return sys_user.phone
     *
     * @mbggenerated
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号
     * sys_user.phone
     *
     * @param phone sys_user.phone
     *
     * @mbggenerated
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 邮箱
     * sys_user.email
     *
     * @return sys_user.email
     *
     * @mbggenerated
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮箱
     * sys_user.email
     *
     * @param email sys_user.email
     *
     * @mbggenerated
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 头像
     * sys_user.status
     *
     * @return sys_user.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 头像
     * sys_user.status
     *
     * @param status sys_user.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 插入时间
     * sys_user.insert_date
     *
     * @return sys_user.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_user.insert_date
     *
     * @param insertDate sys_user.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_user.update_date
     *
     * @return sys_user.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_user.update_date
     *
     * @param updateDate sys_user.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}