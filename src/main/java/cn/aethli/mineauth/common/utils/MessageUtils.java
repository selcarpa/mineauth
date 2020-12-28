package cn.aethli.mineauth.common.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class MessageUtils {
  public static void toOnePlayer(PlayerEntity playerEntity, String content) {
    playerEntity.sendMessage(new StringTextComponent(content), null);
  }

  public static void toOnePlayerByI18n(PlayerEntity playerEntity, String key) {
    playerEntity.sendMessage(new StringTextComponent(I18nUtils.getTranslateContent(key)), null);
  }
}
