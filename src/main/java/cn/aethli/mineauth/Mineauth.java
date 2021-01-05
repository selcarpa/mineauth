package cn.aethli.mineauth;

import cn.aethli.mineauth.config.MineauthConfig;
import cn.aethli.mineauth.handler.AccountHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

@Mod("mineauth")
public class Mineauth {

  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * register this mod and initial some database entity metadata
   *
   * @throws IOException when some jdk internal class exception
   */
  public Mineauth() throws IOException {
    final AccountHandler accountHandler = new AccountHandler();
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MineauthConfig.FORGE_CONFIG_SPEC);
    MinecraftForge.EVENT_BUS.register(accountHandler);
    MinecraftForge.EVENT_BUS.register(this);
    String path = "/assets/mineauth/Banner.txt";
    try (InputStream inputstream = Mineauth.class.getResourceAsStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        LOGGER.info(line);
      }
    } catch (Exception exception) {
      LOGGER.error(FORGEMOD, exception.getMessage(), exception);
    }
  }

  @SubscribeEvent
  public void onFMLServerStartingEvent(FMLServerStartingEvent event) {
    LOGGER.info("All Mineauth module online!");
  }
}
