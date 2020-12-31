package cn.aethli.mineauth.command;

import cn.aethli.mineauth.common.utils.MessageUtils;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.*;

public class LoginHelpCommand extends BaseCommand<AuthPlayer> {
  public static final String command = "LoginHelp";
  private static final List<String> parameters = new ArrayList<>();

  public LoginHelpCommand() {
    super(command, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    msgToOnePlayerByI18n(player, "login_usage");
    return 1;
  }
}
