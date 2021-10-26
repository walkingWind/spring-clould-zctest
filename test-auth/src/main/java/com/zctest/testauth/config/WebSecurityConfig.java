package com.zctest.testauth.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Oauth2提供的默认端点（endpoints）
   * /oauth/authorize：授权端点
   * /oauth/token：令牌端点
   * /oauth/confirm_access：用户确认授权提交端点（这个更准确点说是用来返回授权页面的地址，真的授权按钮提交/oauth/authorize）
   * /oauth/error：授权服务错误信息端点
   * /oauth/check_token：用于资源服务访问的令牌解析端点
   * /oauth/token_key：提供公有密匙的端点，如果使用JWT令牌的话
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
        .antMatchers("/rsa/publicKey").permitAll()
        .anyRequest().authenticated()
         //禁用csrf攻击防御
        .and().csrf().disable()
            //配置更改登录页面
        .formLogin()
            .loginPage("/login")//登录地址
            .loginProcessingUrl("/authentication/form")//登录表单提交地址
            .defaultSuccessUrl("/home")//登录成功返回页面
            .permitAll()
    .and().logout().permitAll();

  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
