package com.mifuns.system.facade.entity;

import com.mifuns.system.facade.enums.ResourceType;

import java.util.Date;

public class SysResource {
    /**
     *
     * sys_resource.resource_id
     *
     * @mbggenerated
     */
    private Long resourceId;

    /**
     * 名称
     * sys_resource.resource_name
     *
     * @mbggenerated
     */
    private String resourceName;

    /**
     * MENU,BUTTON
     * sys_resource.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * 资源路径
     * sys_resource.url
     *
     * @mbggenerated
     */
    private String url;

    /**
     * 父ID
     * sys_resource.parent_id
     *
     * @mbggenerated
     */
    private Long parentId;

    /**
     *
     * sys_resource.parent_ids
     *
     * @mbggenerated
     */
    private String parentIds;

    /**
     *
     * sys_resource.serial_num
     *
     * @mbggenerated
     */
    private Integer serialNum;

    /**
     * 权限
     * sys_resource.permission
     *
     * @mbggenerated
     */
    private String permission;

    /**
     * 1 有效
     * sys_resource.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * 插入时间
     * sys_resource.insert_date
     *
     * @mbggenerated
     */
    private Date insertDate;

    /**
     * 更新时间
     * sys_resource.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;


    public boolean isAvailabled() {
        return status == 1;
    }
    public boolean isButton() {
        return ResourceType.valueOf(type) == ResourceType.BUTTON;
    }

    public boolean isMenu() {
        return ResourceType.valueOf(type) == ResourceType.MENU;
    }

    public SysResource() {
    }

    public SysResource(Long resourceId, Integer status, Date updateDate) {
        this.resourceId = resourceId;
        this.status = status;
        this.updateDate = updateDate;
    }

    /**
     *
     * sys_resource.resource_id
     *
     * @return sys_resource.resource_id
     *
     * @mbggenerated
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     *
     * sys_resource.resource_id
     *
     * @param resourceId sys_resource.resource_id
     *
     * @mbggenerated
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 名称
     * sys_resource.resource_name
     *
     * @return sys_resource.resource_name
     *
     * @mbggenerated
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * 名称
     * sys_resource.resource_name
     *
     * @param resourceName sys_resource.resource_name
     *
     * @mbggenerated
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName == null ? null : resourceName.trim();
    }

    /**
     * MENU,BUTTON
     * sys_resource.type
     *
     * @return sys_resource.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * MENU,BUTTON
     * sys_resource.type
     *
     * @param type sys_resource.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 资源路径
     * sys_resource.url
     *
     * @return sys_resource.url
     *
     * @mbggenerated
     */
    public String getUrl() {
        return url;
    }

    /**
     * 资源路径
     * sys_resource.url
     *
     * @param url sys_resource.url
     *
     * @mbggenerated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 父ID
     * sys_resource.parent_id
     *
     * @return sys_resource.parent_id
     *
     * @mbggenerated
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 父ID
     * sys_resource.parent_id
     *
     * @param parentId sys_resource.parent_id
     *
     * @mbggenerated
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     *
     * sys_resource.parent_ids
     *
     * @return sys_resource.parent_ids
     *
     * @mbggenerated
     */
    public String getParentIds() {
        return parentIds;
    }

    /**
     *
     * sys_resource.parent_ids
     *
     * @param parentIds sys_resource.parent_ids
     *
     * @mbggenerated
     */
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds == null ? null : parentIds.trim();
    }

    /**
     *
     * sys_resource.serial_num
     *
     * @return sys_resource.serial_num
     *
     * @mbggenerated
     */
    public Integer getSerialNum() {
        return serialNum;
    }

    /**
     *
     * sys_resource.serial_num
     *
     * @param serialNum sys_resource.serial_num
     *
     * @mbggenerated
     */
    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    /**
     * 权限
     * sys_resource.permission
     *
     * @return sys_resource.permission
     *
     * @mbggenerated
     */
    public String getPermission() {
        return permission;
    }

    /**
     * 权限
     * sys_resource.permission
     *
     * @param permission sys_resource.permission
     *
     * @mbggenerated
     */
    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    /**
     * 1 有效
     * sys_resource.status
     *
     * @return sys_resource.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1 有效
     * sys_resource.status
     *
     * @param status sys_resource.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 插入时间
     * sys_resource.insert_date
     *
     * @return sys_resource.insert_date
     *
     * @mbggenerated
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 插入时间
     * sys_resource.insert_date
     *
     * @param insertDate sys_resource.insert_date
     *
     * @mbggenerated
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新时间
     * sys_resource.update_date
     *
     * @return sys_resource.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新时间
     * sys_resource.update_date
     *
     * @param updateDate sys_resource.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}