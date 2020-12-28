package cn.aethli.mineauth.common.model;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class PlayerPreparation {
  private PlayerEntity playerEntity;
  private Vector3d vector3d;
  private float rotationYaw;
  private float rotationPitch;
  private boolean registered;

  public PlayerPreparation(
          PlayerEntity playerEntity, Vector3d vector3d, float rotationYaw, float rotationPitch, boolean registered) {
    this.playerEntity = playerEntity;
    this.vector3d = vector3d;
    this.rotationYaw = rotationYaw;
    this.rotationPitch = rotationPitch;
    this.registered = registered;
  }

  public void setRegistered(boolean registered) {
    this.registered = registered;
  }

  public boolean isRegistered() {
    return registered;
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
}
