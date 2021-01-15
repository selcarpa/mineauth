package cn.aethli.mineauth.config;

import cn.aethli.mineauth.command.account.ChangePasswordCommand;
import cn.aethli.mineauth.command.account.RegisterCommand;
import net.minecraftforge.common.ForgeConfigSpec;

public class AccountConfig {

  public final ForgeConfigSpec.BooleanValue enableRegister;
  public final ForgeConfigSpec.BooleanValue enableChangePassword;
  public final ForgeConfigSpec.IntValue delay;
  public final ForgeConfigSpec.ConfigValue<String> defaultPassword;

  public AccountConfig(ForgeConfigSpec.Builder builder) {
    builder.comment("database configuration setting").push("database");
    this.enableRegister =
        builder
            .comment("Enable or disable the /" + RegisterCommand.COMMAND + " command.")
            .define("enableRegister", true);

    this.enableChangePassword =
        builder
            .comment("Enable or disable the /" + ChangePasswordCommand.COMMAND + " command.")
            .define("enableChangePassword", true);

    this.delay =
        builder
            .comment(
                "Delay in seconds a player can authenticate before being automatically kicked from the server.")
            .defineInRange("delay", 60, 30, 600);

    this.defaultPassword =
        builder
            .comment("ResetPassword will set player's password as it.")
            .define("defaultPassword", "Abc123");
    builder.pop();
  }
}
