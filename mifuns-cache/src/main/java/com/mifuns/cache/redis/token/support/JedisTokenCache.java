package com.mifuns.cache.redis.token.support;

import com.mifuns.cache.redis.JedisTemplate;
import com.mifuns.cache.redis.contants.BaseCacheConstant;
import com.mifuns.cache.redis.token.SessionUser;
import com.mifuns.cache.redis.token.TokenCache;
import com.mifuns.cache.redis.token.TokenCacheServiceCode;
import org.apache.commons.lang.StringUtils;

/**
 * Created by miguangying on 2016/12/22.
 */
public class JedisTokenCache implements TokenCache{

    JedisTemplate jedisTemplate;
    private int timeout = TokenCache.DEFAULT_TIMEOUT;



    @Override
    public void expireToken(String userId, TokenCacheServiceCode serviceCode) {
        String oldToken = getToken(userId,serviceCode);
        //获取用户当前的Token，并将 token = userId  删除
        if(StringUtils.isNotEmpty(oldToken)) {
            jedisTemplate.del(BaseCacheConstant.getUserTokenKey(oldToken,serviceCode));
        }
        //将 userId 以 userId = token 删除
        jedisTemplate.del(BaseCacheConstant.getUserTokenKey(userId,serviceCode));
    }


    @Override
    public String setToken(SessionUser user, String token, TokenCacheServiceCode serviceCode) {
        String userId = String.valueOf(user.getUserId());
        //token 以 token = user 的形式存储在缓存中
        jedisTemplate.set(BaseCacheConstant.getUserTokenKey(token,serviceCode), user, getTimeout());//默认90天过期
        //获取用户当前token
        String oldToken = getToken(userId,serviceCode);
        //当前的Token不为空，并将 token = user  删除
        if(StringUtils.isNotEmpty(oldToken) && !oldToken.equals(token)) {
            jedisTemplate.del(BaseCacheConstant.getUserTokenKey(oldToken,serviceCode));
        }
        //userId 以 userId = token 的形式存入缓存中
        jedisTemplate.setStr(BaseCacheConstant.getUserTokenKey(userId,serviceCode), token, getTimeout());//默认90天过期
        return token;
    }


    @Override
    public String getToken(String userId, TokenCacheServiceCode serviceCode) {
        return jedisTemplate.getStr(BaseCacheConstant.getUserTokenKey(userId,serviceCode));
    }


    @Override
    public SessionUser getTokenValue(String token, TokenCacheServiceCode serviceCode) {
        return (SessionUser) jedisTemplate.get(BaseCacheConstant.getUserTokenKey(token,serviceCode));
    }



    @Override
    public boolean existsToken(String token, TokenCacheServiceCode serviceCode) {
        return getTokenValue(token,serviceCode) != null;
    }

    public JedisTokenCache(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    /**
     * 禁用无参实例化
     * @see #JedisTokenCache(JedisTemplate)
     */
    private JedisTokenCache() {}

    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
