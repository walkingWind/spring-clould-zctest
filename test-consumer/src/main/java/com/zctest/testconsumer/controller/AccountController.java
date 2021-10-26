package com.zctest.testconsumer.controller;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("account")
public class AccountController {
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public Object login(HttpServletResponse response) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("username", "xinyues");
        List<String> roles = new ArrayList<>();
        roles.add("Admin");
        roles.add("Dev");
        userInfo.put("roles", roles);
        response.addHeader("AccountInfo", userInfo.toJSONString());

        JSONObject result = new JSONObject();
        result.put("code", 0);
        return result;
    }
    @RequestMapping("main")
    public String toMain() {
        return "main";
    }
    @RequestMapping("getAllAccount")
    @ResponseBody
    public Object getAllAccount() {
        JSONObject userInfo = new JSONObject();
        userInfo.put("username", "xinyues");
        List<String> roles = new ArrayList<>();
        roles.add("Admin");
        roles.add("Dev");
        userInfo.put("roles", roles);
        return userInfo;
    }
}
