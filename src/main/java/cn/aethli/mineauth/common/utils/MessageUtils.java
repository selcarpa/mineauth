package cn.aethli.mineauth.common.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.StringUtils;

public class MessageUtils {
  public static void msgToOnePlayer(PlayerEntity playerEntity, String content) {
    playerEntity.sendMessage(new StringTextComponent(content), playerEntity.getUniqueID());
  }

  public static void msgToOnePlayerByI18n(PlayerEntity playerEntity, String key) {
     playerEntity.sendMessage(new StringTextComponent(StringUtils.stripToEmpty(I18nUtils.getTranslateContent(key))),playerEntity.getUniqueID());
  }
}
