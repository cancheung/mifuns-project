package com.mifuns.cache.redis.token;

import org.springframework.util.Assert;

/**
 * Created by miguangying on 2016/12/22.
 */
public class DefaultTokenCacheManager implements TokenCacheManager {

    private int timeout = TokenCache.DEFAULT_TIMEOUT;
    TokenCache tokenCache;


    @Override
    public TokenCache getTokenCache() {
        return tokenCache;
    }

    @Override
    public void setTokenCache(TokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.tokenCache, "property tokenCache is required");
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        getTokenCache().setTimeout(timeout);
    }
}
