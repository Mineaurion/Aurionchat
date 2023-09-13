package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.config.Channel;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Config {
    private final FileConfiguration config;
    public Rabbitmq rabbitmq = new Rabbitmq();
    public Options options = new Options();
    public Map<String, Channel> channels = new HashMap<>();

    public Config(JavaPlugin plugin){
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        this.setChannels();
        this.rabbitmq.uri = config.getString("rabbitmq.uri");
        this.options.sound = Sound.valueOf(config.getString("options.sound"));
        this.options.spy = config.getBoolean("options.spy");
        this.options.autoMessage = config.getBoolean("options.automessage");
    }

    public static class Rabbitmq {
        public String uri;
    }

    public static final class Options {
        public boolean spy;
        public boolean autoMessage;
        public Sound sound;
    }

    private void setChannels(){
        for (String channel: config.getConfigurationSection("channels").getKeys(false)){
            this.channels.putIfAbsent(
                    channel,
                    new Channel(config.getString("channels." + channel + ".format"), config.getString("channels." + channel + ".alias"))
            );
        }
    }

    public Optional<String> getChannelByNameOrAlias(String search){
        for(Map.Entry<String, Channel> entry: this.channels.entrySet()){
            String name = entry.getKey();
            Channel channel = entry.getValue();
            if(name.equalsIgnoreCase(search) || channel.alias.equalsIgnoreCase(search)){
                return Optional.of(name);
            }
        }
        return Optional.empty();
    }
}
