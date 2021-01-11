package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import cn.aethli.mineauth.entity.AuthPlayer;
import cn.aethli.mineauth.handler.AccountHandler;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class ForgetPassword extends BaseCommand {
  public static final String COMMAND = "forgetPassword";
  private static final Logger LOGGER = LogManager.getLogger();
  private static final List<String> PARAMETERS = new ArrayList<>();
  private static final String filename = "out.txt";

  static {
  }

  public ForgetPassword() {
    super(COMMAND, PARAMETERS);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    CommandSource source = context.getSource();
    PlayerEntity player = source.asPlayer();

    AuthPlayer authPlayer = AccountHandler.getAuthPlayer(player.getUniqueID().toString());
    if (authPlayer != null) {
      msgToOnePlayerByI18n(player, "forget_password_has_login_yet");
    } else {

    }
    return 0;
  }
}
