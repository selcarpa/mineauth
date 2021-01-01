package cn.aethli.mineauth.command;

import cn.aethli.mineauth.entity.BaseEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.Iterator;
import java.util.List;

public abstract class BaseCommand<T extends BaseEntity> implements Command<CommandSource> {
  protected LiteralArgumentBuilder<CommandSource> builder;

  public BaseCommand(String command, List<String> parameters) {
    this.builder = Commands.literal(command);

    if (null != parameters && !parameters.isEmpty()) {
      RequiredArgumentBuilder<CommandSource, String> firstArgument =
          Commands.argument(parameters.get(0), StringArgumentType.string());
      parameters.remove(0);
      if (parameters.isEmpty()) {
        builder = builder.then(firstArgument.executes(this));
      }else {
        RequiredArgumentBuilder<CommandSource, String> lastArgument = firstArgument;
        for (Iterator<String> iterator = parameters.iterator(); iterator.hasNext(); ) {
          String parameter = iterator.next();
          lastArgument =
                  lastArgument.then(
                          iterator.hasNext()
                                  ? Commands.argument(parameter, StringArgumentType.string())
                                  : Commands.argument(parameter, StringArgumentType.string()).executes(this));
        }
        builder.then(firstArgument);
      }
    } else {
      builder.executes(this);
    }
  }

  public LiteralArgumentBuilder<CommandSource> getBuilder() {
    return builder;
  }
}
