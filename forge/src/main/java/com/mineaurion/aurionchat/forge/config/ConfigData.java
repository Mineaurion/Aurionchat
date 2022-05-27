package com.mineaurion.aurionchat.forge.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ConfigData {
    public Rabbitmq rabbitmq;
    public Options options;

    public static class Rabbitmq {
        public ForgeConfigSpec.ConfigValue<String> serverName;
        public ForgeConfigSpec.ConfigValue<String> uri;
    }
    public static final class Options {
        public ForgeConfigSpec.ConfigValue<Boolean> spy;
        public ForgeConfigSpec.ConfigValue<Boolean> autoMessage;
    }

    public Map<String, Channel> getChannels() {
        Map<String, Channel> channels = new HashMap<>();
        try {
            Reader channelsJson = Files.newBufferedReader(FMLPaths.CONFIGDIR.get().resolve("aurionchat-channels.json"));
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Channel>>() {
            }.getType();
            channels = gson.fromJson(channelsJson, type);
            channelsJson.close();
        } catch (IOException exception) {
            System.out.println("Error file aurionchat-channels.json not found, see the readme for more info");
        }
        return channels;
    }
}

