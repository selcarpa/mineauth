package cn.aethli.mineauth.command;

import cn.aethli.mineauth.common.utils.MessageUtils;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.*;

public class RegisterHelpCommand extends BaseCommand<AuthPlayer> {
  public static final String command = "registerHelp";
  private static final List<String> parameters = new ArrayList<>();

  public RegisterHelpCommand() {
    super(command, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();
    msgToOnePlayerByI18n(player, "register_usage");
    return 1;
  }
}
