package cn.aethli.test.mineauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static cn.aethli.mineauth.common.utils.I18nUtils.getLanguageMap;
import static cn.aethli.mineauth.common.utils.I18nUtils.loadLangFile;

public class I18nTest {
  private static final Logger LOGGER = LogManager.getLogger();

  @Test
  public void i18nInitialTest() {
    loadLangFile("en-Us");
    Map<String, String> languageMap = getLanguageMap();
    languageMap.forEach((k, v) -> System.out.println(k + ":" + v));
  }
}
