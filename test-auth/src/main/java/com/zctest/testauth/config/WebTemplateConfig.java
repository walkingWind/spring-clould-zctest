package com.zctest.testauth.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


/**
 * 这里是配合更改auth2登录页用的
 */
@Configuration
public class WebTemplateConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 前面是url路径，后面是视图路径，添加thymeleaf后自动配置prefix为/templates,suffix为.html
        registry.addViewController("/login").setViewName("/login");
//        registry.addViewController("/home").setViewName("/home");
    }


}
