package com.mifuns.system.facade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miguangying on 2017/3/12.
 */
public class PermissionEntity implements Serializable {
    private String username;
    private List<SysResource> resources;
    private List<SysRole> roles;
    private String responseTime;

    public PermissionEntity(String username, List<SysResource> resources, List<SysRole> roles, String responseTime) {
        this.username = username;
        this.resources = resources;
        this.roles = roles;
        this.responseTime = responseTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SysResource> getResources() {
        return resources;
    }

    public void setResources(List<SysResource> resources) {
        this.resources = resources;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "PermissionEntity{" +
                "username='" + username + '\'' +
                ", resources=" + resources +
                ", roles=" + roles +
                ", responseTime='" + responseTime + '\'' +
                '}';
    }
}
