package cn.aethli.mineauth.command;

import cn.aethli.mineauth.entity.BaseEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.List;

public abstract class BaseCommand<T extends BaseEntity> implements Command<CommandSource> {
  protected LiteralArgumentBuilder<CommandSource> builder;

  public BaseCommand(String command,  List<String> parameters) {
    this.builder = Commands.literal(command);
    final LiteralArgumentBuilder<CommandSource>[] thisBuilder =
        new LiteralArgumentBuilder[] {builder};
    if (parameters != null) {
      parameters.forEach(
          parameter ->
              thisBuilder[0] =
                  thisBuilder[0].then(Commands.argument(parameter, StringArgumentType.string())));
    }
    thisBuilder[0].executes(this);
  }

  public LiteralArgumentBuilder<CommandSource> getBuilder() {
    return builder;
  }
}
