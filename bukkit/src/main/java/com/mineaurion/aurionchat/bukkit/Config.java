package com.mineaurion.aurionchat.bukkit;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Set;

public class Config {
    public FileConfiguration config;

    public Config(AurionChat plugin){
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public String getUri(){
        return config.getString("rabbitmq.uri");
    }
    
    public String getFormatChannel(String channelName){
        return config.getString("channels."+ channelName + ".format");
    }

    public Set<String> getAllChannel(){
        return config.getConfigurationSection("channels").getKeys(false);
    }

    public String getChannelAlias(String channel){
        return config.getString("channels."+ channel +".alias");
    }

    public Set<String> getAllAutomessageChannel(){
        Set<String> channels = config.getConfigurationSection("automessage").getKeys(false);
        channels.remove("enable");
        return channels;
    }

    public String getPermissionChannelAutomessage(String channelName){
        return config.getString("automessage." + channelName + ".permission");
    }

    public boolean getConsoleSpy(){
        return config.getBoolean("options.spy");
    }

    public boolean getAutomessageEnable(){
        return config.getBoolean("options.enable");
    }

    public Sound getPingSound(){
        return Sound.valueOf(config.getString("options.sound"));
    }



}
