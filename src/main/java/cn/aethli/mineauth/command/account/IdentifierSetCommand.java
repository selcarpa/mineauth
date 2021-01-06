package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

//// todo we talk about it later
public class IdentifierSetCommand extends BaseCommand {

  private static final List<String> parameters = new ArrayList<>();

  static {
    parameters.add("identifier");
  }

  public IdentifierSetCommand() {
    super("identifierSet", parameters);
  }

  @Override
  public int run(CommandContext<CommandSource> context)
      throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();

    return 0;
  }
}
