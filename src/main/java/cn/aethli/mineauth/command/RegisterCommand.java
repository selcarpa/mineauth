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

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class RegisterCommand extends BaseCommand<AuthPlayer> {
  private static final List<String> parameters = new ArrayList<>();
  private static final Logger LOGGER = LogManager.getLogger(RegisterCommand.class);

  static {
    parameters.add("password");
    parameters.add("confirm");
  }

  public RegisterCommand() {
    super("register", parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    String password = StringArgumentType.getString(context, "password");
    String confirm = StringArgumentType.getString(context, "confirm");
    if (password.equals(confirm)) {
      AuthPlayer authPlayer = new AuthPlayer();
      authPlayer.setUuid(player.getUniqueID().toString());
      //validate if repeat register
      if (DataUtils.selectOne(authPlayer)!=null) {
        msgToOnePlayerByI18n(player,"register_repeat");
        return 0;
      }
      String digestedPassword = DigestUtils.md5Hex(password);
      authPlayer.setPassword(digestedPassword);
      authPlayer.setLastLogin(LocalDateTime.now());
      boolean b = DataUtils.insertOne(authPlayer);
      if (b) {
        Mineauth.addToAuthPlayerMap(player.getUniqueID().toString(), authPlayer);
        msgToOnePlayerByI18n(player, "register_success");
        return 1;
      } else {
        msgToOnePlayerByI18n(player, "error");
        LOGGER.error("Database insert error,{}",new Gson().toJson(authPlayer));
        return 0;
      }
    } else {
      msgToOnePlayerByI18n(player, "password_confirm_error");
      return 0;
    }
  }
}
