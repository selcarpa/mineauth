package cn.aethli.mineauth.config;

import cn.aethli.mineauth.Mineauth;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.common.utils.I18nUtils;
import cn.aethli.mineauth.datasource.ExpansionAbleConnectionPool;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.sql.SQLException;

import static cn.aethli.mineauth.common.utils.DataUtils.initialInternalDatabase;
import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

/**
 * base mineauth config, include enable or not login, register, identifier, change password. and how
 * much time to kick out uncertified user
 */
@Mod.EventBusSubscriber(modid = "mineauth", bus = Mod.EventBusSubscriber.Bus.MOD)
public class MineauthConfig {

  public static final ForgeConfigSpec FORGE_CONFIG_SPEC;
  public static final MineauthConfig MINEAUTH_CONFIG;
  public static final String DEFAULT_H2_DATABASE_FILE_RESOURCE_PATH =
      "/assets/mineauth/initial/internalDatabase.mv.db";
  public static DatabaseConfig databaseConfig;
  public static AccountConfig accountConfig;
  public static LatchConfig latchConfig;

  static {
    final Pair<MineauthConfig, ForgeConfigSpec> specPair =
        new ForgeConfigSpec.Builder().configure(MineauthConfig::new);
    FORGE_CONFIG_SPEC = specPair.getRight();
    MINEAUTH_CONFIG = specPair.getLeft();
  }

  public final ForgeConfigSpec.BooleanValue enableAccountModule;
  public final ForgeConfigSpec.BooleanValue enableLatchModule;
  public final ForgeConfigSpec.ConfigValue<String> language;
  public final ForgeConfigSpec.BooleanValue enableBanner;

  public MineauthConfig(final ForgeConfigSpec.Builder builder) {
    builder.comment("Server configuration").push("server");

    this.enableAccountModule =
        builder
            .comment("Enable or disable account auth module")
            .define("enableAccountModule", true);

    this.enableLatchModule =
        builder.comment("Enable or disable latch module").define("enableLatchModule", false);

    this.language =
        builder.comment("language for message(en-US,zh-CN)").define("language", "en-US");
    this.enableBanner =
        builder.comment("Enable or disable banner on console").define("enableBanner", false);

    builder.pop();

    databaseConfig = new DatabaseConfig(builder);
    accountConfig = new AccountConfig(builder);
    latchConfig = new LatchConfig(builder);
  }

  /** to reset connection pool */
  private synchronized static void afterLoadedConfig() throws SQLException, IOException {
    if (MINEAUTH_CONFIG.enableAccountModule.get()) {
      initialInternalDatabase(DEFAULT_H2_DATABASE_FILE_RESOURCE_PATH);
      ExpansionAbleConnectionPool.init(
          databaseConfig.driver.get(),
          databaseConfig.url.get(),
          databaseConfig.user.get(),
          databaseConfig.password.get(),
          databaseConfig.poolSize.get());
      DataUtils.databaseInit();
      I18nUtils.loadLangFile(MINEAUTH_CONFIG.language.get());
      boolean enableLatchModuleFlag =
          BooleanUtils.toBoolean(MineauthConfig.MINEAUTH_CONFIG.enableLatchModule.get());
      if (enableLatchModuleFlag) {
        MinecraftForge.EVENT_BUS.register(Mineauth.LATCH_HANDLER);
      }else {
        MinecraftForge.EVENT_BUS.unregister(Mineauth.LATCH_HANDLER);
      }
      boolean enableAccountModuleFlag =
          BooleanUtils.toBoolean(MineauthConfig.MINEAUTH_CONFIG.enableAccountModule.get());
      if (enableAccountModuleFlag) {
        MinecraftForge.EVENT_BUS.register(Mineauth.ACCOUNT_HANDLER);
      }else {
        MinecraftForge.EVENT_BUS.unregister(Mineauth.ACCOUNT_HANDLER);
      }
    }
  }

  @SubscribeEvent
  public static void onLoading(final ModConfig.Loading loading)
      throws SQLException, IOException {
    if (loading.getConfig().getFileName().contains("mineauth")) {
      LogManager.getLogger().debug(FORGEMOD, "Mineauth config just got loaded");
      afterLoadedConfig();
    }
  }

  @SubscribeEvent
  public static void onReloading(final ModConfig.Reloading reloading)
      throws SQLException, IOException {
    if (reloading.getConfig().getFileName().contains("mineauth")) {
      LogManager.getLogger().debug(FORGEMOD, "Mineauth config just got changed!");
      afterLoadedConfig();
    }
  }
}
