package cn.aethli.mineauth.command;

import cn.aethli.mineauth.entity.BaseEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseCommand<T extends BaseEntity> implements Command<CommandSource> {
  protected LiteralArgumentBuilder<CommandSource> builder;

  public BaseCommand(String command, List<String> parameters) {
    this.builder = Commands.literal(command);

    if (null != parameters && !parameters.isEmpty()) {
      final AtomicReference<RequiredArgumentBuilder<CommandSource, String>> argument =
          new AtomicReference<>(Commands.argument(parameters.get(0), StringArgumentType.string()));
      parameters.remove(0);
      parameters.forEach(
          parameter ->
              argument.set(
                  argument.get().then(Commands.argument(parameter, StringArgumentType.string()))));
      argument.get().executes(this);
    } else {
      builder.executes(this);
    }
  }

  public LiteralArgumentBuilder<CommandSource> getBuilder() {
    return builder;
  }
}
