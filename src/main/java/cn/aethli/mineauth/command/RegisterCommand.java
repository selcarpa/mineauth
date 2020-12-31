package cn.aethli.mineauth.command;

import cn.aethli.mineauth.Mineauth;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.google.gson.Gson;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class RegisterCommand extends BaseCommand<AuthPlayer> {
  public static final String command = "register";
  public static final Pattern PATTERN;
  private static final List<String> parameters = new ArrayList<>();
  private static final Logger LOGGER = LogManager.getLogger(RegisterCommand.class);
  private static final String REGEX = "^[A-Za-z0-9!#$%]+$";
  private static final Integer PASSWORD_ALLOW_LENGTH = 16;

  static {
    parameters.add("password");
    parameters.add("confirm");
    PATTERN = Pattern.compile(REGEX);
  }

  public RegisterCommand() {
    super(command, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    String password = StringArgumentType.getString(context, "password");
    String confirm = StringArgumentType.getString(context, "confirm");
    if (password.equals(confirm)) {

      Matcher m = PATTERN.matcher(password);
      if (!m.matches() || password.length() > PASSWORD_ALLOW_LENGTH) {
        msgToOnePlayerByI18n(player, "register_password_rule");
        return 1;
      }

      AuthPlayer authPlayer = new AuthPlayer();
      authPlayer.setUuid(player.getUniqueID().toString());
      // validate if repeat register
      if (DataUtils.selectOne(authPlayer) != null) {
        msgToOnePlayerByI18n(player, "register_repeat");
        return 1;
      }
      String digestedPassword = DigestUtils.md5Hex(password);
      authPlayer.setPassword(digestedPassword);
      authPlayer.setLastLogin(LocalDateTime.now());
      boolean b = DataUtils.insertOne(authPlayer);
      if (b) {
        Mineauth.addToAuthPlayerMap(player.getUniqueID().toString(), authPlayer);
        msgToOnePlayerByI18n(player, "register_success");
      } else {
        msgToOnePlayerByI18n(player, "error");
        LOGGER.error("Database insert error,{}", new Gson().toJson(authPlayer));
      }
      return 1;
    } else {
      msgToOnePlayerByI18n(player, "password_confirm_error");
      return 0;
    }
  }
}
