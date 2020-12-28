package cn.aethli.mineauth.command;

import cn.aethli.mineauth.entity.BaseEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.List;
import java.util.function.Consumer;

public abstract class BaseCommand<T extends BaseEntity> implements Command<CommandSource> {
  protected LiteralArgumentBuilder<CommandSource> builder;

  public BaseCommand(String command,  List<String> parameters) {
    this.builder =
        Commands.literal(command);
    if (parameters != null) {
      parameters.forEach(
          parameter -> builder.then(Commands.argument(parameter, StringArgumentType.string())));
    }
    builder.executes(this);
  }

  public LiteralArgumentBuilder<CommandSource> getBuilder() {
    return builder;
  }
}
