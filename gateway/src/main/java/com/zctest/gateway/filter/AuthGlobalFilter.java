package com.zctest.gateway.filter;

import com.nimbusds.jose.JWSObject;

import com.zctest.gateway.config.IgnoreUrlsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.text.ParseException;

import reactor.core.publisher.Mono;

/**
 * Spring Cloud Gateway的Filter的声明周期只有两个：“pre”和“post”：
 PRE：这种过滤器在请求被路由之前调用。可以使用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等
 POST：这种过滤器在路由到微服务以后执行。可以为响应头添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
 怎么来区分是 “pre” 还是 “post” 呢
 return chain.filter(exchange).then(
 Mono.fromRunnable(() -> {
  //todo your code
 })
 );
 chain.filter(exchange) 之前的就是 “pre” 部分，之后的也就是 then 里边的是 “post” 部分
 见 过滤器示例代码.txt
 */

/**
 * gateway过滤器分为局部过滤器和全局过滤器
 * 局部过滤器（GatewayFilter）只对指定url路径有效
 * 全局过滤器（GlobalFilter）对所有路径有效
 *
 * 将登录用户的JWT转化成用户信息的全局过滤器
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

  private final static Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);

  private final IgnoreUrlsConfig ignoreUrlsConfig;

  public AuthGlobalFilter(IgnoreUrlsConfig ignoreUrlsConfig) {
    this.ignoreUrlsConfig = ignoreUrlsConfig;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (token == null || "".equals(token) || ignoreUrlsConfig.getUrls().contains(exchange.getRequest().getURI().getPath())) {
//      ServerHttpRequest request = exchange.getRequest().mutate().header("Authorization", "").build();
//      exchange = exchange.mutate().request(request).build();
      return chain.filter(exchange);
    }
    try {
      /**
       * token只是用来做签名验证的，在服务端并没有存储每次生成的token，但是可以用密钥进行验证
       * token有效期也是写在密文里面的
       */
      // 从token中解析用户信息并设置到Header中去
      String realToken = token.replace("Bearer ", "");
      JWSObject jwsObject = JWSObject.parse(realToken);
      String userStr = jwsObject.getPayload().toString();
      LOGGER.info("AuthGlobalFilter.filter() user:{}", userStr);
      ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
      exchange = exchange.mutate().request(request).build();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return chain.filter(exchange);
  }

  /**
   * 这里是指定过滤器的执行顺序，值越小，优先级越高
   * @return
   */
  @Override
  public int getOrder() {
    return 0;
  }
}
