package com.test.db.test;

import com.test.db.entity.User;
import com.test.db.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by miguangying on 2016/10/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"classpath:spring-db-test.xml"})
public class TestCace {

    @Resource(name="userService")
    UserService userService;



    @Test
    public void test(){
        User user = new User();
        userService.insertUser(user);
    }

}
