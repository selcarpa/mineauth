package cn.aethli.mineauth.common.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/** @author SelcaNyan */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

  private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
  // ignore milliseconds
  private static final DateTimeFormatter DATE_TIME_FORMATTER =  new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .append(DateTimeFormatter.ISO_LOCAL_DATE)
          .appendLiteral(' ')
          .append(DateTimeFormatter.ISO_LOCAL_TIME)
          .toFormatter();

  @Override
  public LocalDateTime valueOf(String src) {
    //    return LocalDateTime.parse(src.substring(0, FORMAT.length()), DATE_TIME_FORMATTER);
    return LocalDateTime.parse(src, DATE_TIME_FORMATTER);
  }

  @Override
  public String parse(Object o) {
    return DATE_TIME_FORMATTER.format((LocalDateTime) o);
  }
}
