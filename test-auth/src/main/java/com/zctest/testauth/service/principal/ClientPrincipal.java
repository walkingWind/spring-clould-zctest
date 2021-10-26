package com.zctest.testauth.service.principal;

import com.zctest.testauth.domain.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Data;

/**
 * 登录用户信息
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/19
 */
@Data
public class ClientPrincipal implements ClientDetails {

  private Client client;

  public ClientPrincipal(Client client) {
    this.client = client;
  }

  @Override
  public String getClientId() {
    return client.getClientId();
  }

  @Override
  public Set<String> getResourceIds() {
    return new HashSet<>(Arrays.asList(client.getResourceIds().split(",")));
  }

  @Override
  public boolean isSecretRequired() {
    return client.getSecretRequire();
  }

  @Override
  public String getClientSecret() {
    return client.getClientSecret();
  }

  @Override
  public boolean isScoped() {
    return client.getSecretRequire();
  }

  @Override
  public Set<String> getScope() {
    return new HashSet<>(Arrays.asList(client.getScope().split(",")));
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return new HashSet<>(Arrays.asList(client.getAuthorizedGrantTypes().split(",")));
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    return new HashSet<>(Arrays.asList(client.getWebServerRedirectUri().split(",")));
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    Arrays.asList(client.getAuthorities().split(",")).forEach(
        auth -> collection.add((GrantedAuthority) () -> auth)
    );
    return collection;
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return client.getAccessTokenValidity();
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return client.getRefreshTokenValidity();
  }

  @Override
  public boolean isAutoApprove(String s) {
    return false;
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return null;
  }
}
