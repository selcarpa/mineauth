package cn.aethli.mineauth.command;

import cn.aethli.mineauth.entity.BaseEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.List;

public abstract class BaseCommand<T extends BaseEntity> implements Command<CommandSource> {
  protected LiteralArgumentBuilder<CommandSource> builder;

  public BaseCommand(String command, List<String> parameters) {
    this.builder = Commands.literal(command);

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
