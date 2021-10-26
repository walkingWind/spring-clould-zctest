package com.zctest.testauth.service.impl;

import com.zctest.testauth.domain.User;
import com.zctest.testauth.service.UserService;
import com.zctest.testauth.service.principal.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


/**
 * 用户管理业务类
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Service
public class UserServiceImpl implements UserService {

  private List<User> userList;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void initData() {
    String password = passwordEncoder.encode("1234567890");
    userList = new ArrayList<>();
    List<String> adminRole = new ArrayList<>();
    adminRole.add("ADMIN");
    List<String> userRole = new ArrayList<>();
    userRole.add("USER");
    List<String> otherRole = new ArrayList<>();
    userRole.add("O1");
    userRole.add("O2");
    userList.add(new User(1L, "admin", password, 1, adminRole));
    userList.add(new User(2L, "zctest", password, 1, userRole));
    userList.add(new User(3L, "other", password, 1, otherRole));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<User> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
    if (findUserList.size() == 0) {
//      throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
      System.out.println("没有找到用户，用户名或密码错误");
    }
    UserPrincipal userPrincipal = new UserPrincipal(findUserList.get(0));
    if (!userPrincipal.isEnabled()) {
//      throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        System.out.println("该账户已被禁用，请联系管理员!");
    } else if (!userPrincipal.isAccountNonLocked()) {
//      throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        System.out.println("该账号已被锁定，请联系管理员!");
    } else if (!userPrincipal.isAccountNonExpired()) {
//      throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        System.out.println("该账号已过期，请联系管理员!");
    } else if (!userPrincipal.isCredentialsNonExpired()) {
//      throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        System.out.println("没有访问权限，请联系管理员!");
    }
    return userPrincipal;
  }

}
