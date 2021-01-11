package cn.aethli.mineauth.handler;

import cn.aethli.mineauth.common.model.LatchPreparation;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author SelcaNyan */
public class LatchHandler {
  public static final ImmutableSet<Block> CONTAINER_BLOCKS =
      ImmutableSet.of(
          Blocks.CHEST, // chest
          Blocks.ENDER_CHEST,
          Blocks.TRAPPED_CHEST,
          Blocks.ACACIA_DOOR, // door
          Blocks.OAK_DOOR,
          Blocks.BIRCH_DOOR,
          Blocks.DARK_OAK_DOOR,
          Blocks.CRIMSON_DOOR,
          Blocks.IRON_DOOR,
          Blocks.JUNGLE_DOOR,
          Blocks.SPRUCE_DOOR,
          Blocks.WARPED_DOOR,
          Blocks.LEVER, // lever
          Blocks.SHULKER_BOX, // shulker box
          Blocks.BLACK_SHULKER_BOX,
          Blocks.BLUE_SHULKER_BOX,
          Blocks.BROWN_SHULKER_BOX,
          Blocks.CYAN_SHULKER_BOX,
          Blocks.GRAY_SHULKER_BOX,
          Blocks.GREEN_SHULKER_BOX,
          Blocks.LIGHT_BLUE_SHULKER_BOX,
          Blocks.LIME_SHULKER_BOX,
          Blocks.MAGENTA_SHULKER_BOX,
          Blocks.ORANGE_SHULKER_BOX,
          Blocks.PINK_SHULKER_BOX,
          Blocks.PURPLE_SHULKER_BOX,
          Blocks.RED_SHULKER_BOX,
          Blocks.WHITE_SHULKER_BOX,
          Blocks.YELLOW_SHULKER_BOX,
          Blocks.BREWING_STAND);
  public static final ImmutableSet<Block> SIGN_BLOCKS =
      ImmutableSet.of(
          Blocks.ACACIA_SIGN,
          Blocks.ACACIA_WALL_SIGN,
          Blocks.WARPED_WALL_SIGN,
          Blocks.BIRCH_WALL_SIGN,
          Blocks.CRIMSON_WALL_SIGN,
          Blocks.DARK_OAK_WALL_SIGN,
          Blocks.JUNGLE_WALL_SIGN,
          Blocks.OAK_WALL_SIGN,
          Blocks.SPRUCE_WALL_SIGN,
          Blocks.BIRCH_SIGN,
          Blocks.CRIMSON_SIGN,
          Blocks.DARK_OAK_SIGN,
          Blocks.JUNGLE_SIGN,
          Blocks.OAK_SIGN,
          Blocks.SPRUCE_SIGN,
          Blocks.WARPED_SIGN);
  private static final Logger LOGGER = LogManager.getLogger();
  private static final Map<String, LatchPreparation> LATCH_PREPARATION_MAP =
      new ConcurrentHashMap<>();

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    final ItemStack itemStack = event.getItemStack();
    final Item item = itemStack.getItem();
    final PlayerEntity player = event.getPlayer();
    if (item instanceof BlockItem && CONTAINER_BLOCKS.contains(((BlockItem) item).getBlock())) {
      // ignore op
      if (player.hasPermissionLevel(1)) {
        return;
      }
      ResourceLocation tag = new ResourceLocation("owner", "mineauth");
      final boolean contains = ItemTags.getCollection().get(tag).contains(item);
      return;
    }
    ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
    if (heldItem.isEmpty()) {
      heldItem = player.getHeldItem(Hand.OFF_HAND);
      if (heldItem.isEmpty()) {
        return;
      }
    }
    if (heldItem.getItem() instanceof BlockItem
        && SIGN_BLOCKS.contains(((BlockItem) heldItem.getItem()).getBlock())) {

    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    final ItemStack itemStack = event.getItemStack();
    final Item item = itemStack.getItem();
    if (item instanceof BlockItem && CONTAINER_BLOCKS.contains(((BlockItem) item).getBlock())) {
      final PlayerEntity player = event.getPlayer();
      final LatchPreparation latchPreparation = new LatchPreparation(player, item);
      LATCH_PREPARATION_MAP.put(player.getUniqueID().toString(), latchPreparation);
    }
  }
}
