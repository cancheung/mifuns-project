package com.mifuns.system.facade.entity;

import java.util.Date;

public class SysApp {
    /**
     *
     * sys_app.app_id
     *
     * @mbggenerated
     */
    private Long appId;

    /**
     * app名称
     * sys_app.app_name
     *
     * @mbggenerated
     */
    private String appName;

    /**
     * appKey
     * sys_app.app_key
     *
     * @mbggenerated
     */
    private String appKey;

    /**
     * app 安全码
     * sys_app.app_secret
     *
     * @mbggenerated
     */
    private String appSecret;

    /**
     * 1 有效
     * sys_app.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * 插入时间
     * sys_app.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_app.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     *
     * sys_app.app_id
     *
     * @return sys_app.app_id
     *
     * @mbggenerated
     */
    public Long getAppId() {
        return appId;
    }

    /**
     *
     * sys_app.app_id
     *
     * @param appId sys_app.app_id
     *
     * @mbggenerated
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * app名称
     * sys_app.app_name
     *
     * @return sys_app.app_name
     *
     * @mbggenerated
     */
    public String getAppName() {
        return appName;
    }

    /**
     * app名称
     * sys_app.app_name
     *
     * @param appName sys_app.app_name
     *
     * @mbggenerated
     */
    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    /**
     * appKey
     * sys_app.app_key
     *
     * @return sys_app.app_key
     *
     * @mbggenerated
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * appKey
     * sys_app.app_key
     *
     * @param appKey sys_app.app_key
     *
     * @mbggenerated
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    /**
     * app 安全码
     * sys_app.app_secret
     *
     * @return sys_app.app_secret
     *
     * @mbggenerated
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * app 安全码
     * sys_app.app_secret
     *
     * @param appSecret sys_app.app_secret
     *
     * @mbggenerated
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    /**
     * 1 有效
     * sys_app.status
     *
     * @return sys_app.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1 有效
     * sys_app.status
     *
     * @param status sys_app.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 插入时间
     * sys_app.insert_date
     *
     * @return sys_app.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_app.insert_date
     *
     * @param insertDate sys_app.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_app.update_date
     *
     * @return sys_app.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_app.update_date
     *
     * @param updateDate sys_app.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}