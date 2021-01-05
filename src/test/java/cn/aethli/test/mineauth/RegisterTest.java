package cn.aethli.test.mineauth;

import cn.aethli.mineauth.command.account.RegisterCommand;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterTest {
  @ParameterizedTest
  @Order(1)
  @ValueSource(
      strings = {
        "ABCDEFGHIG", // true
        "abcdefghig", // true
        "0123456789", // true
        "!@#$%^&*()", // false
        "ABCDEabcde", // true
        "ABCDE01234", // true
        "ABCDE!#$%", // true
        "01234!#$%", // true
        "abcde01234!#$%", // true
        "ABCDE01234!#$%", // true
        "ABCDEabcde!#$%", // true
        "ABCDEabcde01234", // true
        "Aa0!", // true
        "ABCabc012", // true
        "sfsaf*13)", // false
        "密码" // false
      })
  public void patternTest(String password) {

    Pattern pattern = RegisterCommand.PATTERN;
    Matcher matcher = pattern.matcher(password);
    System.out.println(matcher.matches());
  }
}
