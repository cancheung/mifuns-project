package com.mifuns.cache.redis.token;

import java.io.Serializable;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/5/10 </p>
 * <p>Time: 10:59 </p>
 * <p>Version: 1.0 </p>
 */
public class SessionUser implements Serializable{

    private Long userId;
    private String userName;
    /** 此处为手机号码明文 **/
    private String account;
    /** yyyy-MM-dd HH:mm:ss **/
    private String insertDate;
    private String token;
    private String appKey;
    private String openId;

    private Integer cityId;

    /**
     *
     */
    private Integer userType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public SessionUser(Long userId, String userName, String account, String insertDate, String token, String appKey, String openId, Integer cityId, Integer userType) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
        this.insertDate = insertDate;
        this.token = token;
        this.appKey = appKey;
        this.openId = openId;
        this.cityId = cityId;
        this.userType = userType;
    }


    @Override
    public String toString() {
        return "SessionUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", account='" + account + '\'' +
                ", insertDate='" + insertDate + '\'' +
                ", token='" + token + '\'' +
                ", appKey='" + appKey + '\'' +
                ", openId='" + openId + '\'' +
                ", cityId=" + cityId +
                ", userType=" + userType +
                '}';
    }
}
