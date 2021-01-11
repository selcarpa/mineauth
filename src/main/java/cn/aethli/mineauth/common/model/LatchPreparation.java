package cn.aethli.mineauth.common.model;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

/** @author SelcaNyan */
public class LatchPreparation {
  private final PlayerEntity playerEntity;
  private final Item item;

  public LatchPreparation(PlayerEntity playerEntity, Item item) {
    this.playerEntity = playerEntity;
    this.item = item;
  }


  public PlayerEntity getPlayerEntity() {
    return playerEntity;
  }

  public Item getItem() {
    return item;
  }
}
