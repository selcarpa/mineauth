package cn.aethli.mineauth.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DatabaseConfig {

  public final ForgeConfigSpec.ConfigValue<String> columnIdentifier;
  public final ForgeConfigSpec.ConfigValue<String> columnBanned;
  public final ForgeConfigSpec.ConfigValue<String> columnUsername;
  public final ForgeConfigSpec.ConfigValue<String> columnUuid;
  public final ForgeConfigSpec.ConfigValue<String> columnPassword;
  public final ForgeConfigSpec.ConfigValue<String> columnLastLogin;
  public final ForgeConfigSpec.ConfigValue<String> columnIp;
  public final ForgeConfigSpec.ConfigValue<String> columnIpv6;
  public final ForgeConfigSpec.ConfigValue<String> columnForget;
  public final ForgeConfigSpec.ConfigValue<String> url;
  public final ForgeConfigSpec.ConfigValue<String> user;
  public final ForgeConfigSpec.ConfigValue<String> password;
  public final ForgeConfigSpec.ConfigValue<String> table;
  public final ForgeConfigSpec.ConfigValue<String> driver;
  public final ForgeConfigSpec.IntValue poolSize;

  public DatabaseConfig(ForgeConfigSpec.Builder builder) {
    builder.comment("database configuration").push("database");
    this.columnIdentifier =
        builder.comment("Column for the identifier").define("columnIdentifier", "EMAIL");

    this.columnBanned =
        builder
            .comment("Column telling whether the player is banned")
            .define("columnBan", "BANNED");

    this.columnLastLogin =
        builder
            .comment("Column telling whether the player last login time")
            .define("columnLastLogin", "LAST_LOGIN");

    this.columnUsername =
        builder.comment("Column for the username").define("colummUsername", "USERNAME");

    this.columnUuid = builder.comment("Column for UUID").define("colummUuid", "UUID");

    this.columnPassword =
        builder.comment("Column for the encrypted password").define("columnPassword", "PASSWORD");

    this.columnForget = builder.comment("").define("columnForget", "FORGET");

    this.columnIp = builder.comment("").define("columnIp", "IP");

    this.columnIpv6 = builder.comment("").define("columnIpv6", "IPV6");

    this.url =
        builder
            .comment("Server hosting the database(within a clearly schema)")
            .define(
                "url",
                "jdbc:h2:file:./mineauth/internalDatabase;SCHEMA=MINEAUTH;AUTO_SERVER=TRUE;AUTO_RECONNECT=TRUE");

    this.driver =
        builder
            .comment(
                "JDBC driver to use(\"org.h2.Driver\" for h2 database,\"org.mariadb.jdbc.Driver\" for mysql/mariadb)")
            .define("driver", "org.h2.Driver");

    this.user = builder.comment("Database user").define("user", "root");

    this.password = builder.comment("Database password").define("password", "admin");

    this.table = builder.comment("Table to be used").define("table", "PLAYERS");

    this.poolSize = builder.comment("PoolSize to be used").defineInRange("poolSize", 2, 1, 10);

    builder.pop();
  }
}
