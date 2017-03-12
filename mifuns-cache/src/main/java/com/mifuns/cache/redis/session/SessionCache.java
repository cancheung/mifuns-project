package com.mifuns.cache.redis.session;

import java.io.Serializable;

/**
 * @author Stony Created Date : 2016/4/23  17:39
 */
public interface SessionCache extends Serializable{


    public int setSession(String key, Object value, int seconds);

    public Object getSession(String key);

    public int delSession(String key);

    boolean exists(String key);

    String warpKey(String key);
}
