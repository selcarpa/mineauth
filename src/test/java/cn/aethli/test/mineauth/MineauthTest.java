package cn.aethli.test.mineauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class MineauthTest {
  private static final Logger LOGGER = LogManager.getLogger();

  @Test
  public void startupBannerTest() {
    String path = "/assets/mineauth/Banner.txt";
    try (InputStream inputstream = MineauthTest.class.getResourceAsStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception exception) {
      LOGGER.error(FORGEMOD, exception.getMessage(), exception);
    }
  }
}
