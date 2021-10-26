package com.zctest.testconsumer.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author zhengcan
 * @Date 2021/7/15 17:00
 * @Version 1.0
 * @Description
 */
@FeignClient(value = "test-provider" )
public interface TestRemote {

    @GetMapping("/hi")
    String test(@RequestParam("name") String name);
}
