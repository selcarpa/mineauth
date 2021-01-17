package cn.aethli.mineauth.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class I18nUtils {
  private static final Map<String, String> LANGUAGE_MAP = new ConcurrentHashMap<>();
  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  /** @param language language file name with out ".json" */
  public static void loadLangFile(String language) {
    String path = "/assets/mineauth/json/i18n/" + language.trim() + ".json";
    try (InputStream inputstream = I18nUtils.class.getResourceAsStream(path)) {
      if (inputstream != null) {
        LANGUAGE_MAP.putAll(
            gson.fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), Map.class));
      } else {
        path = "mineauth/i18n/" + language.trim() + ".json";
        File file = new File(path);
        try (InputStream extraInputStream = new FileInputStream(file)) {
          LANGUAGE_MAP.putAll(
              gson.fromJson(
                  new InputStreamReader(extraInputStream, StandardCharsets.UTF_8), Map.class));
        }
      }
    } catch (Exception e) {
      LogManager.getLogger().debug(FORGEMOD, e.getMessage(), e);
    }
  }

  public static String getTranslateContent(String key) {
    return LANGUAGE_MAP.get(key);
  }

  public static Map<String, String> getLanguageMap() {
    return LANGUAGE_MAP;
  }
}
