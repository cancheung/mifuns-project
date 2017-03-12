package com.mifuns.cache.redis.token;

/**
 * Created by miguangying on 2016/12/22.
 */
public interface TokenCache {
    /** 单位秒 **/
    public static final int DEFAULT_TIMEOUT = (86400 * 90); //90天过期



    /**
     * <p>添加用户Token 方法：</p>
     * <p>1.token 以 token = user (SessionUser)的形式存储在缓存中</p>
     * <p>2.获取用户当前的Token，并将 token = user  删除</p>
     * <p>3.userId 以 userId = token 的形式存入缓存中</p>
     * @param user
     * @param token
     * @param serviceCode 类型
     * @see TokenCacheServiceCode
     * @return
     */
    public String setToken(SessionUser user, String token, TokenCacheServiceCode serviceCode);



    /**
     * 根据用户ID返回当前Token
     * @param userId
     * @param serviceCode 类型
     * @see TokenCacheServiceCode
     * @return
     */
    public String getToken(String userId, TokenCacheServiceCode serviceCode);


    /**
     * 用户Token是否存在
     * @param token
     * @param serviceCode 类型
     * @see TokenCacheServiceCode
     * @return
     */
    public boolean existsToken(String token, TokenCacheServiceCode serviceCode);




    /**
     * 返回会话用户
     * @param token
     * @param serviceCode 类型
     * @return SessionUser
     */
    public SessionUser getTokenValue(String token, TokenCacheServiceCode serviceCode);

    public void setTimeout(int timeout);



    public void expireToken(String userId, TokenCacheServiceCode serviceCode);
}
