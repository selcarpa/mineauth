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

  private static final List<String> parameters = new ArrayList<>();

  static {
    parameters.add("identifier");
  }

  public IdentifierSetCommand() {
    super(COMMAND, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();

    AuthPlayer authPlayer = AccountHandler.getAuthPlayer(player.getUniqueID().toString());
    if (authPlayer == null) {
      msgToOnePlayerByI18n(player, "identifier_not_login_yet", player.getScoreboardName());
    } else {
      String identifier = StringArgumentType.getString(context, "identifier");
      authPlayer.setIdentifier(identifier);
      DataUtils.updateById(authPlayer);
    }
    return 0;
  }
}
