package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.config.MineauthConfig;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class ResetPasswordCommand extends BaseCommand {
  public static final String COMMAND = "resetPassword";
  private static final List<String> PARAMETERS = new ArrayList<>();

  static {
    PARAMETERS.add("userName");
  }

  public ResetPasswordCommand() {
    super(COMMAND, PARAMETERS, 4);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    final CommandSource source = context.getSource();
    ServerPlayerEntity player = source.asPlayer();
    if (player.hasPermissionLevel(4)) {
      String userName = StringArgumentType.getString(context, "userName");
      AuthPlayer authPlayer = new AuthPlayer();
      authPlayer.setUsername(userName);
      authPlayer = DataUtils.selectOne(authPlayer);
      if (authPlayer == null) {
        msgToOnePlayerByI18n(player, "login_not_found", userName);
      } else {
        final String defaultPassword = MineauthConfig.accountConfig.defaultPassword.get();
        final String digestedPassword = DigestUtils.md5Hex(defaultPassword);
        authPlayer.setPassword(digestedPassword);
        DataUtils.updateById(authPlayer);
      }
      return 1;
    } else {
      msgToOnePlayerByI18n(player, "permission_deny");
      return 0;
    }
  }
}
