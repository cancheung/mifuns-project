package com.mifuns.cache.redis.session.support;


import com.mifuns.cache.redis.JedisTemplate;
import com.mifuns.cache.redis.session.SessionCache;

/**
 * @author Stony Created Date : 2016/4/23  17:46
 */
public class JedisSessionCache implements SessionCache {

    private JedisTemplate jedisTemplate;

    public JedisSessionCache(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }
    private JedisSessionCache(){}

    @Override
    public int setSession(String key, Object value,int seconds) {
        jedisTemplate.set(warpKey(key),value,seconds);
        return 0;
    }

    @Override
    public Object getSession(String key) {
        return jedisTemplate.get(warpKey(key));
    }

    @Override
    public int delSession(String key) {
        jedisTemplate.del(warpKey(key));
        return 1;
    }

    @Override
    public boolean exists(String key) {
        return jedisTemplate.exists(warpKey(key));
    }

    @Override
    public String warpKey(String key) {
        return MAGIC +  key;
    }

    public static final String MAGIC = "Mi_Sid_";
}
