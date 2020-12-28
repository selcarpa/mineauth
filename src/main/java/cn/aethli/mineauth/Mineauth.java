package cn.aethli.mineauth;

import cn.aethli.mineauth.annotation.MetadataScan;
import cn.aethli.mineauth.command.ChangePasswordCommand;
import cn.aethli.mineauth.command.LoginCommand;
import cn.aethli.mineauth.command.RegisterCommand;
import cn.aethli.mineauth.common.model.PlayerPreparation;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.common.utils.MetadataUtils;
import cn.aethli.mineauth.config.MineauthConfig;
import cn.aethli.mineauth.entity.AuthPlayer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static cn.aethli.mineauth.common.utils.DataUtils.initialInternalDatabase;
import static cn.aethli.mineauth.common.utils.MessageUtils.msgToOnePlayerByI18n;
import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

@Mod("mineauth")
@MetadataScan(packageName = ("cn.aethli.mineauth.entity"))
public class Mineauth {

  public static final String DEFAULT_H2_DATABASE_FILE_RESOURCE_PATH =
      "/assets/mineauth/initial/internalDatabase.mv.db";
  private static final Map<String, AuthPlayer> AUTH_PLAYER_MAP = new ConcurrentHashMap<>();
  private static final Map<String, PlayerPreparation> PLAYER_PREPARATION_MAP =
      new ConcurrentHashMap<>();
  private static final Logger LOGGER = LogManager.getLogger();

  private static final List<String> allowCommands = new ArrayList<>();

  /**
   * register this mod and initial some database entity metadata
   *
   * @throws IOException when some jdk internal class exception
   * @throws ClassNotFoundException when some jdk internal class exception
   */
  public Mineauth() throws IOException, ClassNotFoundException {
    Class<Mineauth> mineauthClass = Mineauth.class;
    if (mineauthClass.isAnnotationPresent(MetadataScan.class)) {
      MetadataScan metadataScan = mineauthClass.getAnnotation(MetadataScan.class);
      for (String packageName : metadataScan.packageName()) {
        MetadataUtils.initMetadata(packageName);
      }
    }

    initialInternalDatabase(DEFAULT_H2_DATABASE_FILE_RESOURCE_PATH);

    //    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MineauthConfig.FORGE_CONFIG_SPEC);
    MinecraftForge.EVENT_BUS.register(this);
  }

  /**
   * auth player
   *
   * @param key player unique id
   * @param authPlayer @see cn.aethli.mineauth.entity.AuthPlayer
   */
  public static void addToAuthPlayerMap(String key, AuthPlayer authPlayer) {
    AUTH_PLAYER_MAP.put(key, authPlayer);
    PLAYER_PREPARATION_MAP.remove(key);
  }

  private void handleLivingEvents(LivingEvent event) {
    if (event.getEntity() instanceof PlayerEntity
        && event.isCancelable()
        && PLAYER_PREPARATION_MAP.containsKey(event.getEntity().getUniqueID().toString())) {
      msgToOnePlayerByI18n((PlayerEntity) event.getEntity(), "login_welcome");
      event.setCanceled(true);
    }
  }

  /**
   * hold on player's position and rotation parameter, to cancel player's movement event and reset
   * to initial state
   *
   * @param event event
   */
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
    PlayerEntity player = event.getPlayer();
    PlayerPreparation playerPreparation =
        new PlayerPreparation(
            player, player.getPositionVec(), player.rotationYaw, player.rotationPitch, false);
    String playerId = player.getUniqueID().toString();
    AUTH_PLAYER_MAP.remove(playerId);
    PLAYER_PREPARATION_MAP.put(playerId, playerPreparation);
    msgToOnePlayerByI18n(player,"welcome");
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
    AUTH_PLAYER_MAP.remove(event.getPlayer().getUniqueID().toString());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingAttackEvent(LivingAttackEvent event) {
    handleLivingEvents(event);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingDeathEvent(LivingDeathEvent event) {
    handleLivingEvents(event);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {
    handleLivingEvents(event);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingHealEvent(LivingHealEvent event) {
    handleLivingEvents(event);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingHurtEvent(LivingHurtEvent event) {
    handleLivingEvents(event);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingSetTargetAttackEvent(LivingSetAttackTargetEvent event) {
    if (event.getTarget() instanceof PlayerEntity
        && PLAYER_PREPARATION_MAP.containsKey(event.getTarget().getUniqueID().toString())) {
      event.getEntityLiving().setRevengeTarget(null);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onPlayerEvent(PlayerEvent event) {
    PlayerEntity player = event.getPlayer();
    if (!AUTH_PLAYER_MAP.containsKey(player.getUniqueID().toString()) && event.isCancelable()) {
      event.setCanceled(true);
      msgToOnePlayerByI18n(player, "login_welcome");
    }
  }

  @SubscribeEvent
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    PlayerEntity player = event.getPlayer();
    if (!AUTH_PLAYER_MAP.containsKey(player.getUniqueID().toString())
        && event.getSide() == LogicalSide.SERVER) {
      event.setCanceled(true);
      msgToOnePlayerByI18n(player, "login_welcome");
    }
  }

  @SubscribeEvent
  public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
    PlayerEntity player = event.player;
    if (!AUTH_PLAYER_MAP.containsKey(player.getUniqueID().toString())
        && event.side == LogicalSide.SERVER) {
      PlayerPreparation playerPreparation =
          PLAYER_PREPARATION_MAP.get(event.player.getUniqueID().toString());
      Vector3d vector3d = playerPreparation.getVector3d();
      ((ServerPlayerEntity) event.player)
          .connection.setPlayerLocation(
              vector3d.getX(),
              vector3d.getY(),
              vector3d.getZ(),
              playerPreparation.getRotationYaw(),
              playerPreparation.getRotationPitch());
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onServerChatEvent(ServerChatEvent event) {
    PlayerEntity player = event.getPlayer();
    if (event.isCancelable() && !AUTH_PLAYER_MAP.containsKey(player.getUniqueID().toString())) {
      event.setCanceled(true);
      msgToOnePlayerByI18n(player, "login_welcome");
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onItemTossEvent(ItemTossEvent event) {
    PlayerEntity player = event.getPlayer();
    if (event.isCancelable() && !AUTH_PLAYER_MAP.containsKey(player.getUniqueID().toString())) {
      // avoid toss item without login
      player.inventory.addItemStackToInventory(event.getEntityItem().getItem());
      event.setCanceled(true);
      msgToOnePlayerByI18n(player, "login_welcome");
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onCommandEvent(CommandEvent event) throws CommandSyntaxException {
    String name = event.getParseResults().getContext().getNodes().get(0).getNode().getName();
    CommandSource source = event.getParseResults().getContext().getSource();
    if (source.getEntity() instanceof ServerPlayerEntity) {
      PlayerEntity player = source.asPlayer();
      if (!allowCommands.contains(name)&&!AUTH_PLAYER_MAP.containsKey(player.getUniqueID().toString()) && event.isCancelable()) {
        LOGGER.info(
            "Player {} tried to execute /{} without being logged in.",
            player.getName().getString(),
            name);
        event.setCanceled(true);
        msgToOnePlayerByI18n(player, "login_welcome");
      }
    }
  }

  @SubscribeEvent
  public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
    event.getDispatcher().register(new RegisterCommand().getBuilder());
    allowCommands.add(RegisterCommand.command);
    event.getDispatcher().register(new LoginCommand().getBuilder());
    allowCommands.add(LoginCommand.command);
    event.getDispatcher().register(new ChangePasswordCommand().getBuilder());
    allowCommands.add(ChangePasswordCommand.command);
  }

  @SubscribeEvent
  public void onFMLServerStartingEvent(FMLServerStartingEvent event) {
    LOGGER.info("All Mineauth module online!");
    String path = "/assets/mineauth/Banner.txt";
    try (InputStream inputstream = Mineauth.class.getResourceAsStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        LOGGER.info(line);
      }
    } catch (Exception exception) {
      LOGGER.error(FORGEMOD, exception.getMessage(), exception);
    }
  }
}
