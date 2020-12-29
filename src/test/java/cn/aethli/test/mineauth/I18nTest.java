package cn.aethli.test.mineauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static cn.aethli.mineauth.common.utils.I18nUtils.getLanguageMap;
import static cn.aethli.mineauth.common.utils.I18nUtils.loadLangFile;
import static cn.aethli.test.mineauth.utils.PrintUtils.jsonPrint;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class I18nTest {

  @Test
  @Order(1)
  public void i18nInitialTest() {
    loadLangFile("en-US");
    Map<String, String> languageMap = getLanguageMap();
    jsonPrint(languageMap);
  }

  @ParameterizedTest
  @Order(2)
  @ValueSource(strings = {"zh-CN"})
  public void i18nSwitchToOtherLanguage(String fileName) {
    loadLangFile(fileName);
    Map<String, String> languageMap = getLanguageMap();
    jsonPrint(languageMap);
  }
}
