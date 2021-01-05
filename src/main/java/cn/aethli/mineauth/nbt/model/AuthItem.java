package cn.aethli.mineauth.nbt.model;

public class AuthItem {

  public static final Integer AUTH_LEVEL_OWNER = 0;
  public static final Integer AUTH_LEVEL_AUTHED = 1;

  private final String uuid;
  private final Integer authLevel;

  public AuthItem(final String uuid, final Integer authLevel) {
    this.uuid = uuid;
    this.authLevel = authLevel;
  }

  public String getUuid() {
    return uuid;
  }

  public Integer getAuthLevel() {
    return authLevel;
  }
}
