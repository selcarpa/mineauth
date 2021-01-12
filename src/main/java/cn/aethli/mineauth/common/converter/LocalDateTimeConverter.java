package cn.aethli.mineauth.common.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** @author SelcaNyan */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

  // ignore milliseconds
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public LocalDateTime valueOf(String src) {
    return LocalDateTime.parse(src.substring(0, 19), DATE_TIME_FORMATTER);
  }

  @Override
  public String parse(Object o) {
    return DATE_TIME_FORMATTER.format((LocalDateTime) o);
  }
}
