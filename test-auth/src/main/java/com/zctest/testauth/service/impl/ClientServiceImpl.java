package com.zctest.testauth.service.impl;

import com.zctest.testauth.domain.Client;
import com.zctest.testauth.service.ClientService;
import com.zctest.testauth.service.principal.ClientPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


/**
 * 客户端管理业务类
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/18
 */
@Service
public class ClientServiceImpl implements ClientService {

  private List<Client> clientList;
  private final PasswordEncoder passwordEncoder;

  public ClientServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }



  @PostConstruct
  public void initData() {

    String clientSecret = passwordEncoder.encode("666");
    clientList = new ArrayList<>();
    // 1、密码模式  这种模式虽然不安全，但是我觉得可以用于内部信任的客户端
    // 不想暴露clientSecret的情况下，可以在后端进行自行填充加入，前端依旧无感登录
//    clientList.add(Client.builder()
//        .clientId("client-app")
//        .resourceIds("oauth2-resource")
//        .secretRequire(false)
//        .clientSecret(clientSecret)
//        .scopeRequire(false)
//        .scope("all")
//        .authorizedGrantTypes("password,refresh_token")
//        .authorities("ADMIN,USER")
//        .accessTokenValidity(3600)
//        .refreshTokenValidity(86400).build());


    /**
     * 授权密模式的应用场景应该是一个三方资源模式
     *
     * 比如，用户、公司A和公司B
     * 用户将照片存储在了公司A的服务器上
     * 公司B想访问用户存储的照片
     * 那么，此时，公司A就适合部署一套auth2的授权码模式
     *
     *  大致流程就是
     *  用户在公司B开发的应用上，想访问储存在公司A上的照片
     *  1.在公司B的app上向公司A发起请求，请求redirect_uri填写公司B的app地址，能让授权后返回本页面
     *  2.页面跳转公司A提供的登录页
     *  3.用户通过用户名和密码在公司A的应用上登录
     *  4.登录后进入公司A的授权界面
     *  5.点击授权后，页面回到公司B的设定返回地址
     *  6.此时，公司B的页面地址上多了code
     *  7.公司B可以拿着code进行其他操作了
     *
     */
    // 2、授权码模式
    clientList.add(Client.builder()
        .clientId("client-app-2")
        .resourceIds("oauth2-resource2")//暂时没用
        .secretRequire(false)//暂时没用
        .clientSecret(clientSecret)
        .scopeRequire(true)//暂时没用
        .scope("admin")//暂时没用all、admin
        .authorizedGrantTypes("authorization_code,refresh_token")
        .webServerRedirectUri("https://www.gathub.cn,https://www.baidu.com,http://localhost:8081/login")
        .authorities("USER")//暂时没用
        .accessTokenValidity(3600)
        .refreshTokenValidity(86400).build());

    //3.隐式授权（授权模式的简化版本，登录后就返回token）
    //经过测试，依然需要用户点击授权按钮的，另外返回的东西都在url里，有点不安全
    /**
     * 返回的信息包含
     * access_token    token值
     * token_type      token类型
     * expires_in      超时时间
     * scope           作用域
     * jti             不知道什么（类似标识一样的uuid）
     * 没有了refresh_token
     */
    clientList.add(Client.builder()
            .clientId("client-app-3")
            .resourceIds("oauth2-resource3")//暂时没用
            .secretRequire(false)//暂时没用
            .clientSecret(clientSecret)
            .scopeRequire(false)//暂时没用
            .scope("visitor")//暂时没用（"admin", "visitor"） 当客户端参数传入scope时，会验证是否和服务端设置的匹配，多个用,隔开
            .authorizedGrantTypes("implicit")
            .webServerRedirectUri("https://www.gathub.cn")
            .authorities("USER")//暂时没用
            .accessTokenValidity(3600)
            .refreshTokenValidity(86400).build());




  }

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    List<Client> findClientList = clientList.stream().filter(item -> item.getClientId().equals(clientId)).collect(Collectors.toList());
    if (findClientList.size()==0) {
//      throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstant.NOT_FOUND_CLIENT);
      System.out.println("没有找到客户端");


    }
    return new ClientPrincipal(findClientList.get(0));
  }
}
