package com.mineaurion.aurionchat.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        builder.comment("Config for Aurionchat").push("rabbitmq");
        builder.comment("Must Be unique between all of your servers").define("serverName", "ServerName");
        builder.define("uri", "amqp://guest:guest@localhost:5672/");
        builder.pop();

        builder.push("options");
        builder.comment("Allow console to print all message").define("spy", false);
        builder.define("autoMessage", false);
        builder.pop();

        SPEC = builder.build();
    }


}

