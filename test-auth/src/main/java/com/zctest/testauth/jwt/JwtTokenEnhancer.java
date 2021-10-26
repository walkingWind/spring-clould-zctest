package com.zctest.testauth.jwt;

import com.zctest.testauth.service.principal.UserPrincipal;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



/**
 * JWT内容增强器
 *  可以将一些自定义的东西放入
 *  通过验证后调用
 *
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    Map<String, Object> info = new HashMap<>();
    // 把用户ID设置到JWT中
    info.put("id", userPrincipal.getId());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
    return accessToken;
  }
}
