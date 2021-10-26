package com.zctest.testauth.service.impl;

import com.zctest.testauth.service.ResourceService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;



/**
 * 资源与角色匹配关系管理业务类
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Service
public class ResourceServiceImpl implements ResourceService {

  private final RedisTemplate<String, Object> redisTemplate;

  public ResourceServiceImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }


    /**
     * Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。

     通常我们会是在Spring框架中使用到@PostConstruct注解 该注解的方法在整个Bean初始化中的执行顺序：

     Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
  @PostConstruct
  public void initData() {
      List<String> adminRole = new ArrayList<>();
      adminRole.add("ADMIN");
      List<String> userRole = new ArrayList<>();
      userRole.add("USER");
      userRole.add("ADMIN");
    Map<String, List<String>> resourceRolesMap = new TreeMap<>();
    resourceRolesMap.put("/resource/hello", adminRole);
    resourceRolesMap.put("/resource/currentUser", userRole);
    redisTemplate.opsForHash().putAll("role_map", resourceRolesMap);
  }
}
