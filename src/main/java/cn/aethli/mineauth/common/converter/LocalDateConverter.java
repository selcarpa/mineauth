package cn.aethli.mineauth.common.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<LocalDateTime> {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public LocalDateTime valueOf(String src) {
    return LocalDateTime.parse(src, DATE_TIME_FORMATTER);
  }

  @Override
  public String parse(Object o) {
    return DATE_TIME_FORMATTER.format((LocalDateTime)o);
  }
}
