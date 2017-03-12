package com.mifuns.cache.redis.token;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by miguangying on 2016/12/22.
 */
public interface TokenCacheManager extends InitializingBean {
    public TokenCache getTokenCache();

    public void setTokenCache(TokenCache tokenCache);
}
