package cn.aethli.mineauth.common.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingFormatArgumentException;

public class MessageUtils {
  private static final Logger LOGGER = LogManager.getLogger();

  public static void msgToOnePlayer(final PlayerEntity playerEntity, final String content) {
    playerEntity.sendMessage(new StringTextComponent(content), playerEntity.getUniqueID());
  }

  /**
   * seed a message to one player
   * @param playerEntity playerEntity
   * @param key message key in json
   * @param args String::format arguments
   */
  public static void msgToOnePlayerByI18n(
      final PlayerEntity playerEntity, final String key, final Object... args) {
    try {
      msgToOnePlayer(
          playerEntity,
          String.format(StringUtils.stripToEmpty(I18nUtils.getTranslateContent(key)), args));
    } catch (MissingFormatArgumentException e) {
      LOGGER.error(e.getMessage(), e);
      msgToOnePlayer(
          playerEntity,
          formatString(StringUtils.stripToEmpty(I18nUtils.getTranslateContent(key)), args));
    }
  }

  /**
   * Way to format String and avoid MissingFormatArgumentException(copy from stackoverflow)
   *
   * @param stringToFormat origin string
   * @param args arguments to replace format specifier
   * @return string
   */
  public static String formatString(final String stringToFormat, final Object... args) {
    if (stringToFormat == null || stringToFormat.length() == 0) return stringToFormat;
    int specifiersCount = 0;
    final int argsCount = args == null ? 0 : args.length;
    final StringBuilder sb = new StringBuilder(stringToFormat.length());
    for (int i = 0; i < stringToFormat.length(); ++i) {
      char c = stringToFormat.charAt(i);
      if (c != '%') sb.append(c);
      else {
        final char nextChar = stringToFormat.charAt(i + 1);
        if (nextChar == '%' || nextChar == 'n') {
          ++i;
          sb.append(c);
          sb.append(nextChar);
          continue;
        }
        // found a specifier
        ++specifiersCount;
        if (specifiersCount <= argsCount) sb.append(c);
        else
          while (true) {
            ++i;
            c = stringToFormat.charAt(i);
            // find the end of the converter, to ignore it all
            if (c == 't' || c == 'T') {
              // time prefix and then a character, so skip it
              ++i;
              break;
            }
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') break;
          }
      }
    }
    return String.format(sb.toString(), args);
  }
}
