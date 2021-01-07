package cn.aethli.mineauth.command.latch;

import cn.aethli.mineauth.command.BaseCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public class ChangeOwnerCommand extends BaseCommand {
  public static final String COMMAND = "changeOwner";
  private static final List<String> parameters = new ArrayList<>();

  static {
    parameters.add("name");
  }

  public ChangeOwnerCommand() {
    super(COMMAND, parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return 1;
  }
}
