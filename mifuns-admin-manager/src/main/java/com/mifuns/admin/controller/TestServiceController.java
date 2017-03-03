package com.mifuns.admin.controller;

import com.mifuns.customer.facade.service.test.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguangying on 2016/12/22.
 */
@Controller
public class TestServiceController {

    @Resource
    TestService testService;

    @RequestMapping("/testservice")
    @ResponseBody
    public Object testService(){
        Map<String,Object> data = new HashMap<String,Object>();
        String str = testService.insertService();
        data.put("result",str);
        return data;
    }
}
