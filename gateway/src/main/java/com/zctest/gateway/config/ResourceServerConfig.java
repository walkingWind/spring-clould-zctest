package com.zctest.gateway.config;

import com.zctest.gateway.authorization.AuthorizationManager;
import com.zctest.gateway.component.RestAuthenticationEntryPoint;
import com.zctest.gateway.component.RestfulAccessDeniedHandler;
import com.zctest.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;


/**
 * 资源服务器配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
  private final AuthorizationManager authorizationManager;
  private final IgnoreUrlsConfig ignoreUrlsConfig;
  private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    // 1、自定义处理JWT请求头过期或签名错误的结果(客户端未授权)
    http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);

//    // 2、对白名单路径，直接移除JWT请求头（鉴权管理器会先执行，然后GlobalFilter执行，当需要执行在鉴权管理器前面时，可加入如下代码）
//    http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);


    http.authorizeExchange()
        //配置不通过鉴权管理器的白名单
        .pathMatchers(StringUtils.join(ignoreUrlsConfig.getUrls(),",").split(",")).permitAll() // 白名单配置
        .anyExchange().access(authorizationManager) // 鉴权管理器配置
        .and().exceptionHandling()
         //客户端token获取成功，没有通过自定义的authorizationManager鉴权管理器配置的
        .accessDeniedHandler(restfulAccessDeniedHandler)
         //客户端token都没有的
        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and().csrf().disable();
    return http.build();
  }

  @Bean
  public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //自定义权限前缀（不写默认前缀SCOPE_）
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    //获取权限信息时候用的key
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
  }

}
