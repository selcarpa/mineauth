package cn.aethli.mineauth.common.model;

import cn.aethli.mineauth.entity.BaseEntity;
import net.minecraft.entity.player.PlayerEntity;

public class CommandParameter<T extends BaseEntity> {

  public T entity;
  public PlayerEntity playerEntity;
}
