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

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

/** @author 93162 */
public class UnbanCommand extends BaseCommand {
  public static final String COMMAND = "SmurfCheck";
  private static final List<String> PARAMETERS = new ArrayList<>();

  static {
    PARAMETERS.add("userName");
  }

  public UnbanCommand() {
    super(COMMAND, PARAMETERS, 1);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    final CommandSource source = context.getSource();
    ServerPlayerEntity player = source.asPlayer();
    if (!player.hasPermissionLevel(MineauthConfig.accountConfig.permissionLevel.get())) {
      msgToOnePlayerByI18n(player, "permission_deny");
    } else {
      String userName = StringArgumentType.getString(context, "userName");
      AuthPlayer authPlayer = new AuthPlayer();
      authPlayer.setUsername(userName);
      authPlayer = DataUtils.selectOne(authPlayer);
      if (authPlayer == null) {
        msgToOnePlayerByI18n(player, "login_not_found", userName);
      } else {
        authPlayer.setBanned(false);
        DataUtils.updateById(authPlayer);
      }
    }
    return 1;
  }
}
