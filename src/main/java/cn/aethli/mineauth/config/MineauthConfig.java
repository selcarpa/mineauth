package cn.aethli.mineauth.config;

import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.datasource.ExpansionAbleConnectionPool;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.sql.SQLException;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

/**
 * base mineauth config, include enable or not login, register, identifier, change password. and how
 * much time to kick out uncertified user
 */
@Mod.EventBusSubscriber(modid = "mineauth", bus = Mod.EventBusSubscriber.Bus.MOD)
public class MineauthConfig {

  public static final ForgeConfigSpec FORGE_CONFIG_SPEC;
  public static final MineauthConfig MINEAUTH_CONFIG;
  public static DatabaseConfig databaseConfig;

  static {
    final Pair<MineauthConfig, ForgeConfigSpec> specPair =
        new ForgeConfigSpec.Builder().configure(MineauthConfig::new);
    FORGE_CONFIG_SPEC = specPair.getRight();
    MINEAUTH_CONFIG = specPair.getLeft();
  }

  public final ForgeConfigSpec.BooleanValue identifierRequired;
  public final ForgeConfigSpec.BooleanValue enableLogin;
  public final ForgeConfigSpec.BooleanValue enableRegister;
  public final ForgeConfigSpec.BooleanValue enableChangePassword;
  public final ForgeConfigSpec.IntValue delay;

  public MineauthConfig(final ForgeConfigSpec.Builder builder) {
    builder.comment("Server configuration settings").push("server");

    this.identifierRequired =
        builder
            .comment("Identifier must be provided for registration and authentication")
            .define("identifierRequired", false);

    this.enableLogin =
        builder
            .comment(
                "Enable or disable the /login command. If disabled, the server will be opened to everyone).")
            .define("enableLogin", true);

    this.enableRegister =
        builder.comment("Enable or disable the /register command.").define("enableRegister", true);

    this.enableChangePassword =
        builder
            .comment("Enable or disable the /changepassword command.")
            .define("enableChangePassword", true);

    this.delay =
        builder
            .comment(
                "delay in seconds a player can authenticate before being automatically kicked from the server.")
            .defineInRange("delay", 60, 1, 1024);
    builder.pop();

    databaseConfig = new DatabaseConfig(builder);
  }

  /** to reset connection pool */
  private static void afterLoadedConfig() throws SQLException, ClassNotFoundException {
    ExpansionAbleConnectionPool.init(
        databaseConfig.driver.get(),
        databaseConfig.url.get(),
        databaseConfig.user.get(),
        databaseConfig.password.get(),
        databaseConfig.poolSize.get());
    DataUtils.DatabaseInit();
  }

  @SubscribeEvent
  public static void onLoad(final ModConfig.Loading configEvent) throws SQLException, ClassNotFoundException {
    if (configEvent.getConfig().getFileName().contains("mineauth")) {
      LogManager.getLogger().debug(FORGEMOD, "Loaded mineauth config file");
      afterLoadedConfig();
    }
  }

  @SubscribeEvent
  public static void onFileChange(final ModConfig.Reloading configEvent) throws SQLException, ClassNotFoundException {
    if (configEvent.getConfig().getFileName().contains("mineauth")) {
      LogManager.getLogger().debug(FORGEMOD, "Forge config just got changed on the file system!");
      afterLoadedConfig();
    }
  }
}
