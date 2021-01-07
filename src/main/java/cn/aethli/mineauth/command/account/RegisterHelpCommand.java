package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.*;

public class RegisterHelpCommand extends BaseCommand {
  public static final String COMMAND = "registerHelp";
  private static final List<String> parameters = new ArrayList<>();

  public RegisterHelpCommand() {
    super(COMMAND, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    msgToOnePlayerByI18n(player, "register_usage");
    return 1;
  }
}
