package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.config.MineauthConfig;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;

/** @author 93162 */
public class BanSmurfCommand extends BaseCommand {
  public static final String COMMAND = "banSmurf";
  private static final List<String> PARAMETERS = new ArrayList<>();

  static {
  }

  public BanSmurfCommand() {
    super(COMMAND, PARAMETERS);
  }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
      final CommandSource source = context.getSource();
      ServerPlayerEntity player = source.asPlayer();
      if (!player.hasPermissionLevel(MineauthConfig.accountConfig.permissionLevel.get())) {
        msgToOnePlayerByI18n(player, "permission_deny");
      } else {
        String userName = StringArgumentType.getString(context, "userName");
        AuthPlayer authPlayer = new AuthPlayer();
        authPlayer.setUsername(userName);
        authPlayer = DataUtils.selectOne(authPlayer);
        if (authPlayer == null) {
          msgToOnePlayerByI18n(player, "login_not_found", userName);
        } else {
          List<AuthPlayer> authPlayers = new ArrayList<>();
          String ip = authPlayer.getIp();
          if (StringUtils.isNotEmpty(ip)) {
            authPlayer = new AuthPlayer();
            authPlayer.setIp(ip);
            authPlayers.addAll(DataUtils.select(authPlayer));
          }
          String ipv6 = authPlayer.getIpv6();
          if (StringUtils.isNotEmpty(ipv6)) {
            authPlayer = new AuthPlayer();
            authPlayer.setIpv6(ipv6);
            authPlayers.addAll(DataUtils.select(authPlayer));
          }
          if (!authPlayers.isEmpty()) {
          String uuids =
              authPlayers.stream().map(AuthPlayer::getUuid).collect(Collectors.joining("','","'","'"));
          String sql =
              "update "
                  + MineauthConfig.databaseConfig.table.get()
                  + "set "
                  + MineauthConfig.databaseConfig.columnBanned
                  + "='true' where "
                  + MineauthConfig.databaseConfig.columnUuid
                  + " in ("
                  + uuids
                  + ")";
            try {
              DataUtils.executeUpdate(sql);
            } catch (SQLException e) {
              e.printStackTrace();
            }
          } else {
            msgToOnePlayerByI18n(player, "smurf_empty");
          }
        }
      }
      return 1;
    }
}

