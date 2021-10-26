package com.zctest.testconsumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.zctest.testconsumer.domain.User;
import com.zctest.testconsumer.remote.TestRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author zhengcan
 * @Date 2021/7/15 16:58
 * @Version 1.0
 * @Description
 */
@RestController
public class TestController {
    @Autowired
    private TestRemote testRemote;

    /**
     * user权限或者admin权限
     * @return
     */
    @GetMapping("/user")
    public String test1(){
        return "user can pass";
    }

    /**
     * 不需要权限
     * @return
     */
    @GetMapping("/nologin")
    public String test2(){
        return "all can pass";
    }

    /**
     * admin权限
     * @return
     */
    @GetMapping("/admin")
    public String test3(){
        return "admin can pass";
    }


    @GetMapping("/hello")
    public String test(@RequestParam("name") String name){
        return testRemote.test(name);
    }

    @GetMapping("/currentUser")
    public User currentUser(HttpServletRequest request) {
        // 从Header中获取用户信息
        String userStr = request.getHeader("user");
        JSONObject userJsonObject = JSONObject.parseObject(userStr);
        return User.builder()
                .username(userJsonObject.getString("user_name"))
                .id(userJsonObject.getLong("id"))
                .roles(Arrays.asList(userJsonObject.getString("authorities"))).build();
    }

}
