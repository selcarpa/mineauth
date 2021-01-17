package cn.aethli.mineauth.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LatchConfig {
    public LatchConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("latch module configuration").push("latch");
        builder.pop();
    }
}
