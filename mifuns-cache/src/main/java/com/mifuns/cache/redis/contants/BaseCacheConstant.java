package com.mifuns.cache.redis.contants;


import com.mifuns.cache.redis.token.TokenCacheServiceCode;

/**
 * @author Stony Created Date : 2016/4/20  17:36
 */
public abstract class BaseCacheConstant {

    public static final String KEY_USER_TOKEN = "UserToken";
    public static final String KEY_PHONE_PASSWD = "PhonePasswd";


    public static String getUserTokenKey(String token){
        return KEY_USER_TOKEN + "_" + token;
    }

    /**
     *
     * @param token
     * @param serviceCode 服务代码
     * @return
     */
    public static String getUserTokenKey(String token, TokenCacheServiceCode serviceCode){
        return serviceCode + "_" +  KEY_USER_TOKEN + "_" + token;
    }

    public static  String getPhoneVerificationCodeKey(String phone){
        return "PhoneVerifyCode_" + phone;
    }

}
