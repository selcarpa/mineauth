package cn.aethli.mineauth.common.utils;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class I18nUtils {
  private static final Map<String, String> LANGUAGE_MAP = new ConcurrentHashMap<>();

  public static void loadLangFile(String language) {
    String path = "/assets/mineauth/json/i18n/" + language.trim() + ".json";
    try (InputStream inputstream = I18nUtils.class.getResourceAsStream(path)) {
      LANGUAGE_MAP.putAll(
          (new Gson())
              .fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), Map.class));
    } catch (Exception e) {
      LogManager.getLogger().debug(FORGEMOD, e.getMessage(),e);
    }
  }

  public static String getTranslateContent(String key) {
    return LANGUAGE_MAP.get(key);
  }

  public static Map<String, String> getLanguageMap() {
    return LANGUAGE_MAP;
  }
}
