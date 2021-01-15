package cn.aethli.mineauth.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LatchConfig {
    public LatchConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("database configuration setting").push("database");
        builder.pop();
    }
}
