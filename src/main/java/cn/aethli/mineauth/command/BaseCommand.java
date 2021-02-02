package cn.aethli.mineauth.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.List;

public abstract class BaseCommand implements Command<CommandSource> {
  protected LiteralArgumentBuilder<CommandSource> builder;

  protected BaseCommand(String command, List<String> parameters) {
    this(command, parameters, 0);
  }

  protected BaseCommand(String command, List<String> parameters, int permissionLevel) {
    this.builder = Commands.literal(command);
    builder.requires(commandSource -> commandSource.hasPermissionLevel(permissionLevel));
    if (null != parameters && !parameters.isEmpty()) {
      builder.then(getArgument(parameters));
    } else {
      builder.executes(this);
    }
  }

  private RequiredArgumentBuilder<CommandSource, String> getArgument(List<String> parameters) {
    String parameter = parameters.get(0);
    if (parameters.size() > 1) {
      parameters.remove(0);
      return Commands.argument(parameter, StringArgumentType.string())
          .then(getArgument(parameters));
    } else {
      return Commands.argument(parameter, StringArgumentType.string()).executes(this);
    }
  }

  public LiteralArgumentBuilder<CommandSource> getBuilder() {
    return builder;
  }
}
