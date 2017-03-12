package com.mifuns.security.shiro.session;


import com.mifuns.cache.redis.session.SessionCache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/4/26 </p>
 * <p>Time: 16:04 </p>
 * <p>Version: 1.0 </p>
 */
public class SecuritySessionDAO extends CachingSessionDAO implements Serializable, InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(SecuritySessionDAO.class);

    private SessionCache sessionCache;
    private int timeOut = 86400; //1天
    private final int SESSION_TIMEOUT = timeOut * 14;
    public static final String KEY_SESSION_ID_LIST = "blueSessionIdList";
    private String appKey;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        sessionCache.setSession(String.valueOf(sessionId), session, timeOut);
        putSessionIds(String.valueOf(sessionId));
        logger.debug("create session appKey[{}] of session [{}] ", appKey, session);
        return session.getId();
    }

    /**
     * 会话ID list
     * @param sessionId
     */
    private void putSessionIds(String sessionId) {
        Set<String> sessionIds = (Set<String>) sessionCache.getSession(KEY_SESSION_ID_LIST);
        if(sessionIds == null){
            sessionIds = new HashSet<String>();
        }
        sessionIds.add(sessionId);
        sessionCache.setSession(KEY_SESSION_ID_LIST, sessionIds, SESSION_TIMEOUT);
        logger.trace("sessionIds size = {}", sessionIds.size());
        logger.trace("sessionIds = {}", sessionIds);
    }
    private void delSessionIds(String sessionId) {
        Set<String> sessionIds = (Set<String>) sessionCache.getSession(KEY_SESSION_ID_LIST);
        if(sessionIds == null){
            return;
        }
        sessionIds.remove(sessionId);
        sessionCache.setSession(KEY_SESSION_ID_LIST, sessionIds, SESSION_TIMEOUT);
        logger.trace("sessionIds size = {}", sessionIds.size());
        logger.trace("sessionIds = {}", sessionIds);

    }

    @Override
    protected void doUpdate(Session session) {
        //过期会话删除
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            delete(session);
            return;
        }
        logger.trace("update appKey[{}] of session [{}] ", appKey, session);
        sessionCache.setSession(String.valueOf(session.getId()), session, timeOut);
    }
    @Override
    public void update(Session session) throws UnknownSessionException {
        doUpdate(session);
    }
    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> sessionIds = (Set<String>) sessionCache.getSession(KEY_SESSION_ID_LIST);
        if(sessionIds == null) {
            return Collections.EMPTY_LIST;
        }
        List<Session> activeSessions = new ArrayList<Session>();
        for(String sessionId : sessionIds){
            if(sessionCache.exists(sessionId)){
                activeSessions.add((Session) sessionCache.getSession(sessionId));
            }
        }
        //将存活的放入sessionIdList
        Set<String> useSessionIds = new HashSet<>();
        for(Session session : activeSessions){
            useSessionIds.add(String.valueOf(session.getId()));
        }
        sessionCache.setSession(KEY_SESSION_ID_LIST, useSessionIds, SESSION_TIMEOUT);
        logger.trace("activeSessions size = {}", activeSessions.size());
        logger.trace("activeSessions  = {}", activeSessions);
        return activeSessions;
    }
    @Override
    public void delete(Session session) {
        doDelete(session);
    }
    @Override
    protected void doDelete(Session session) {
        logger.debug("delete appKey[{}] of session [{}] ", appKey, session);
        sessionCache.delSession(String.valueOf(session.getId()));
        delSessionIds(String.valueOf(session.getId()));
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session =  (Session) sessionCache.getSession(String.valueOf(sessionId));
        logger.trace("read Session appKey[{}] of session [{}] ", appKey, session);
        return session;
    }


    public void setSessionCache(SessionCache sessionCache) {
        this.sessionCache = sessionCache;
    }


    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sessionCache, "Property 'sessionCache' is required");
        Assert.notNull(appKey, "Property 'appKey' is required");
    }
}