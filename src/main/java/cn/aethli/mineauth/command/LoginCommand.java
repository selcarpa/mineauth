package cn.aethli.mineauth.command;

import cn.aethli.mineauth.Mineauth;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class LoginCommand extends BaseCommand<AuthPlayer> {
  private static final List<String> parameters = new ArrayList<>();
  public static final String command = "login";

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
    String password = StringArgumentType.getString(context, "password");
    AuthPlayer authPlayer = new AuthPlayer();
    authPlayer.setUuid(player.getUniqueID().toString());
    // MD5 is enough
    String digestedPassword = DigestUtils.md5Hex(password);
    authPlayer.setPassword(digestedPassword);
    authPlayer = DataUtils.selectOne(authPlayer);
    if (authPlayer != null) {
      Mineauth.addToAuthPlayerMap(player.getUniqueID().toString(), authPlayer);
    } else {
      msgToOnePlayerByI18n(player, "login_wrong_password");
    }
    return 1;
  }
}
