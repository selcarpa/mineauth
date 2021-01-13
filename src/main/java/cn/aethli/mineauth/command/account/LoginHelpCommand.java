package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class LoginHelpCommand extends BaseCommand {
  public static final String COMMAND = "loginHelp";
  private static final List<String> PARAMETERS = new ArrayList<>();

  public LoginHelpCommand() {
    super(COMMAND, PARAMETERS);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    msgToOnePlayerByI18n(player, "login_usage");
    return 1;
  }
}
