package com.zctest.gateway.authorization;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 *
 * 自定义鉴权逻辑
 *
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
  private final RedisTemplate<String, Object> redisTemplate;

  public AuthorizationManager(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
    // 1、从Redis中获取当前路径可访问角色列表
    URI uri = authorizationContext.getExchange().getRequest().getURI();
    List<String> authorities = (List<String>)redisTemplate.opsForHash().get("role_map", uri.getPath());

      if(authorities!=null && authorities.size()>0){
        //Spring security会给所有角色添加上前缀，所以校验的时候也要添加，否则校验不成功(默认前缀SCOPE_)
        authorities = authorities.stream().map(i -> i = "ROLE_" + i).collect(Collectors.toList());
      }else {
        authorities = new ArrayList<>();
      }

    // 2、认证通过且角色匹配的用户可访问当前路径
    return mono
        .filter(Authentication::isAuthenticated)
        .flatMapIterable(Authentication::getAuthorities)
        .map(GrantedAuthority::getAuthority)
        .any(authorities::contains)
        .map(AuthorizationDecision::new)
        .defaultIfEmpty(new AuthorizationDecision(false));
  }

}
