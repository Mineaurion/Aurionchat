package com.mineaurion.aurionchat.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Config {
    public FileConfiguration config;

    public Boolean setup;

    public Set<String> channels;
    public String host;
    public String consoleSpy;

    public Config(AurionChat plugin){
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void load(){
        host       = config.getString("rabbitmq.host");
        channels   = getAllChannel();
        consoleSpy = getConsoleSpy();
    }

    public String getFormatChannel(String channelName){
        return config.getString("Channels."+ channelName + ".format");
    }


    private String getConsoleSpy(){
        return config.getString("Console.spy");
    }

    private Set<String> getAllChannel(){
        return config.getConfigurationSection("Channels").getKeys(false);
    }


}
