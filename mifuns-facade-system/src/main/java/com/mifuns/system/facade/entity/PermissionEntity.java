package com.mifuns.system.facade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miguangying on 2017/3/12.
 */
public class PermissionEntity implements Serializable {
    private String username;
    private List<Resource> resources;
    private List<Role> roles;
    private String responseTime;

    public PermissionEntity(String username, List<Resource> resources, List<Role> roles, String responseTime) {
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

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
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
