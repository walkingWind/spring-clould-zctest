package com.zctest.testauth.config;

import com.zctest.testauth.jwt.JwtTokenEnhancer;
import com.zctest.testauth.service.ClientService;
import com.zctest.testauth.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

/**
 * 认证服务器配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

  private final UserService userService;
  private final ClientService clientService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenEnhancer jwtTokenEnhancer;

  /**
   * 客户端信息配置
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//    clients.inMemory().withClient("1").scopes("").autoApprove();

//    clients.inMemory()
//        // 1、密码模式
//        .withClient("client-app")
//        .secret(passwordEncoder.encode("123456"))
//        .scopes("read,write")
//        .authorizedGrantTypes("password", "refresh_token")
//        .accessTokenValiditySeconds(3600)
//        .refreshTokenValiditySeconds(86400)
//        .and()
//        // 2、授权码授权
//        .withClient("client-app-2")
//        .secret(passwordEncoder.encode("123456"))
//        .scopes("read")
//        .authorizedGrantTypes("authorization_code", "refresh_token")
//        .accessTokenValiditySeconds(3600)
//        .refreshTokenValiditySeconds(86400)
//        .redirectUris("https://www.gathub.cn", "https://www.baidu.com");
    clients.withClientDetails(clientService);
  }


  /**
   * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
    List<TokenEnhancer> delegates = new ArrayList<>();
    delegates.add(jwtTokenEnhancer);
    delegates.add(accessTokenConverter());
    enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(userService) //配置加载用户信息的服务
        .accessTokenConverter(accessTokenConverter())
        .tokenEnhancer(enhancerChain);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security.allowFormAuthenticationForClients();
    //单点登录的身份验证配置,这句很重要，不然单点登录服务都启动不了
    security.tokenKeyAccess("isAuthenticated()");
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setKeyPair(keyPair());
    return jwtAccessTokenConverter;
  }

  /**
   * 证书通过jdk生成，在jdk bin目录下执行
   * keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
   * @return
   */
  @Bean
  public KeyPair keyPair() {
    // 从classpath下的证书中获取秘钥对
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
    return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
  }

}
