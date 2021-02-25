package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.config.MineauthConfig;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayer;
import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

/** @author 93162 */
public class MSqlCommand extends BaseCommand {
  public static final String COMMAND = "msql";
  private static final List<String> PARAMETERS = new ArrayList<>();
  private static final List<String> PREFIX = new ArrayList<>();
  private static final Logger LOGGER = LogManager.getLogger();

  static {
    PARAMETERS.add("sql");
    PREFIX.add("update");
    PREFIX.add("delete");
    PREFIX.add("select");
    PREFIX.add("insert");
  }

  public MSqlCommand() {
    super(COMMAND, PARAMETERS, 4);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    if (MineauthConfig.accountConfig.enableMSql.get() == null
        && !MineauthConfig.accountConfig.enableMSql.get()) {
      return 0;
    }
    final CommandSource source = context.getSource();
    ServerPlayerEntity player = source.asPlayer();
    if (!player.hasPermissionLevel(MineauthConfig.accountConfig.permissionLevel.get())) {
      msgToOnePlayerByI18n(player, "permission_deny");
    } else {
      String sql = StringArgumentType.getString(context, "sql");
      String prefix = sql.substring(0, 6);
      if (!PREFIX.contains(prefix.toLowerCase())) {
        msgToOnePlayerByI18n(player, "sql_not_support");
        if (prefix.equalsIgnoreCase("select")) {
          try {
            List<Map<String, String>> maps = DataUtils.executeSelect(sql);
            if (!maps.isEmpty()) {
              Set<String> columnNames = maps.get(0).keySet();
              msgToOnePlayerByI18n(player, "sql_execute_success");
              msgToOnePlayer(player, String.join(",", columnNames));
              maps.forEach(
                  r -> {
                    String column =
                        columnNames.stream()
                            .map(c -> String.valueOf(r.get(c)))
                            .collect(Collectors.joining(","));
                    msgToOnePlayer(player, column);
                  });
            }
          } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            msgToOnePlayerByI18n(player, "sql_execute_fail", e.getMessage());
          }
        } else {
          try {
            int effectCount = DataUtils.executeUpdate(sql);
            msgToOnePlayerByI18n(player, "sql_execute_success");
            msgToOnePlayerByI18n(player, "sql_effect_count", effectCount);
          } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            msgToOnePlayerByI18n(player, "sql_execute_fail", e.getMessage());
          }
        }
      }
    }
    return 1;
  }
}
