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

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class IdentifierSetCommand extends BaseCommand {
  public static final String COMMAND = "identifierSet";

  private static final List<String> PARAMETERS = new ArrayList<>();

  static {
    PARAMETERS.add("identifier");
  }

  public IdentifierSetCommand() {
    super(COMMAND, PARAMETERS);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();

    AuthPlayer authPlayer = AccountHandler.getAuthPlayer(player.getUniqueID().toString());
    if (authPlayer == null) {
      msgToOnePlayerByI18n(player, "identifier_not_login_yet", player.getScoreboardName());
    } else {
      //todo avoid select
      authPlayer.setUuid(player.getUniqueID().toString());
      authPlayer = DataUtils.selectOne(authPlayer);
      if (authPlayer == null) {
        return 0;
      }
      String identifier = StringArgumentType.getString(context, "identifier");
      authPlayer.setIdentifier(identifier);
      DataUtils.updateById(authPlayer);
      msgToOnePlayerByI18n(player, "identifier_set_success");
    }
    return 1;
  }
}
