package com.mifuns.cache.redis.session;

/**
 * @author Stony Created Date : 2016/4/23  17:42
 */
public class DefaultSessionCacheManager implements SessionCacheManager{

    private SessionCache sessionCache;

    public DefaultSessionCacheManager(SessionCache sessionCache) {
        this.sessionCache = sessionCache;
    }
    private DefaultSessionCacheManager(){}

    @Override
    public SessionCache getSessionCache() {
        return sessionCache;
    }
}
