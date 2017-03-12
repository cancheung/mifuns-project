package com.test.jedis;

import com.mifuns.cache.redis.JedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by miguangying on 2016/12/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"classpath:spring-redis-test.xml"})
public class JedisTemplateTest {

    private static final Logger logger = LoggerFactory.getLogger(JedisTemplateTest.class);

    @Resource(name="jedisTemplate")
    JedisTemplate jedisTemplate;


    @Test
    public void testStr(){
        jedisTemplate.set("test",12,10);
        logger.info("*********获取缓存***{}",jedisTemplate.get("test"));
    }
}
