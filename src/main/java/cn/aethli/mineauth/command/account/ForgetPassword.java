package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.entity.AuthPlayer;
import cn.aethli.mineauth.handler.AccountHandler;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

public class ForgetPassword extends BaseCommand {
  public static final String COMMAND = "forgetPassword";
  private static final Logger LOGGER = LogManager.getLogger();
  private static final List<String> PARAMETERS = new ArrayList<>();
  private static final String PATH_FILE_NAME = "./mineauth/forget.txt";
  private static RandomAccessFile randomAccessFile = null;
  private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();


  static {
  }

  public ForgetPassword() {
    super(COMMAND, PARAMETERS);
  }

  private static RandomAccessFile getRandomAccessFile() {
    if (randomAccessFile == null) {
      try {
        File file = new File(PATH_FILE_NAME);
        // create mineauth dir
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdir();
        }
        if (!file.exists()) {
          file.createNewFile();
        }
        randomAccessFile = new RandomAccessFile(file, "rwd");
        return randomAccessFile;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    } else {
      return randomAccessFile;
    }
  }

  public static void closeRandomAccessFile() throws IOException {
    if (randomAccessFile != null) {
      randomAccessFile.close();
    }
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    final CommandSource source = context.getSource();
    final PlayerEntity player = source.asPlayer();

    AuthPlayer authPlayer = AccountHandler.getAuthPlayer(player.getUniqueID().toString());
    if (authPlayer != null) {
      msgToOnePlayerByI18n(player, "forget_password_has_login_yet");
    } else {
      authPlayer = new AuthPlayer();
      authPlayer.setUuid(player.getUniqueID().toString());
      authPlayer = DataUtils.selectOne(authPlayer);
      if (authPlayer == null) {
        msgToOnePlayerByI18n(player, "login_not_found", player.getScoreboardName());
        return 1;
      }
      final RandomAccessFile randomAccessFile = getRandomAccessFile();
      if (randomAccessFile == null) {
        msgToOnePlayerByI18n(player, "error");
        return 1;
      } else {
        StringBuilder forgetContent = new StringBuilder();
        try {
          forgetContent
              .append(LocalDateTime.now().toInstant(ZONE_OFFSET).toEpochMilli())
              .append("|")
              .append(player.getUniqueID().toString())
              .append("|")
              .append(player.getScoreboardName())
              .append("|")
              .append(authPlayer.getIdentifier())
              .append("|");
          randomAccessFile.seek(randomAccessFile.length());
          randomAccessFile.write(forgetContent.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
          LOGGER.error(e.getMessage());
          LOGGER.debug(e.getMessage(), e);
        }
      }
    }
    return 1;
  }
}
