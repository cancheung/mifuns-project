package com.mifuns.admin.controller;

import com.mifuns.customer.facade.test.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by miguangying on 2016/12/22.
 */
@Controller
public class TestServiceController {

    @Resource
    TestService testService;

    @RequestMapping("/testservice")
    @ResponseBody
    public String testService(){
        return testService.insertService();
    }
}
