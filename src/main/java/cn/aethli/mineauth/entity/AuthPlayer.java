package cn.aethli.mineauth.entity;

import java.time.LocalDateTime;

/** player information */
public class AuthPlayer extends BaseEntity {

  private String username;
  private String uuid;
  private String password;
  private LocalDateTime lastLogin;
  private Boolean banned;
  private String identifier;
  private String ip;
  private Boolean forget;
  private Boolean ipv6;

  public Boolean getIpv6() {
    return ipv6;
  }

  public void setIpv6(Boolean ipv6) {
    this.ipv6 = ipv6;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Boolean getForget() {
    return forget;
  }

  public void setForget(Boolean forget) {
    this.forget = forget;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Boolean getBanned() {
    return banned;
  }

  public void setBanned(Boolean banned) {
    this.banned = banned;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
