package cn.aethli.mineauth.common.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** @author SelcaNyan */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

  private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
  // ignore milliseconds
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

  @Override
  public LocalDateTime valueOf(String src) {
    return LocalDateTime.parse(src.substring(0, FORMAT.length()), DATE_TIME_FORMATTER);
  }

  @Override
  public String parse(Object o) {
    return DATE_TIME_FORMATTER.format((LocalDateTime) o);
  }
}
