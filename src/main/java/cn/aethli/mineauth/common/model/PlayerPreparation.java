package cn.aethli.mineauth.common.model;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class PlayerPreparation {
  private final PlayerEntity playerEntity;
  private final Vector3d vector3d;
  private final float rotationYaw;
  private final float rotationPitch;
  private final Integer foodLevel;

  public PlayerPreparation(
      PlayerEntity playerEntity,
      Vector3d vector3d,
      float rotationYaw,
      float rotationPitch,
      Integer foodLevel) {
    this.playerEntity = playerEntity;
    this.vector3d = vector3d;
    this.rotationYaw = rotationYaw;
    this.rotationPitch = rotationPitch;
    this.foodLevel = foodLevel;
  }

  public PlayerEntity getPlayerEntity() {
    return playerEntity;
  }

  public Vector3d getVector3d() {
    return vector3d;
  }

  public float getRotationYaw() {
    return rotationYaw;
  }

  public float getRotationPitch() {
    return rotationPitch;
  }

  public Integer getFoodLevel() {
    return foodLevel;
  }
}
