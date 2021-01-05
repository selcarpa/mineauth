package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.entity.AuthPlayer;
import cn.aethli.mineauth.handler.AccountHandler;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class LoginCommand extends BaseCommand {
  public static final String command = "login";
  private static final List<String> parameters = new ArrayList<>();

  static {
    parameters.add("password");
  }

  public LoginCommand() {
    super(command, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    AuthPlayer authPlayer = new AuthPlayer();
    authPlayer.setUuid(player.getUniqueID().toString());
    authPlayer = DataUtils.selectOne(authPlayer);
    if (authPlayer == null) {
      msgToOnePlayerByI18n(player, "login_not_found");
    } else {
      if (authPlayer.getBanned()) {
        msgToOnePlayerByI18n(player,"banned");
      }
      String password = StringArgumentType.getString(context, "password");
      String digestedPassword = DigestUtils.md5Hex(password);
      if (authPlayer.getPassword().equals(digestedPassword)) {
        AccountHandler.addToAuthPlayerMap(player.getUniqueID().toString(), authPlayer);
        msgToOnePlayerByI18n(player, "login_success");
      } else {
        msgToOnePlayerByI18n(player, "login_wrong_password");
      }
    }
    return 1;
  }
}
