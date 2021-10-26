package com.zctest.ssotest.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author zhengcan
 * @Date 2021/7/15 16:58
 * @Version 1.0
 * @Description
 */
@RestController
public class TestController {

    @GetMapping("/user")
    public Authentication currentUser(Authentication authentication) {

        return authentication;

        // 从Header中获取用户信息
//        String userStr = request.getHeader("user");
//        JSONObject userJsonObject = JSONObject.parseObject(userStr);
//        return MyUser.builder()
//                .username(userJsonObject.getString("user_name"))
//                .id(userJsonObject.getLong("id"))
//                .roles(Arrays.asList(userJsonObject.getString("authorities"))).build();
    }

}
