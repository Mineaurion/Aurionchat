package com.mineaurion.aurionchat.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Set;

public class Config {
    public FileConfiguration config;

    public Boolean setup;

    public Set<String> channels;
    public String host;
    public String servername;

    public Config(AurionChat plugin){
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void load(){
        host       = config.getString("rabbitmq.host");
        channels   = getAllChannel();
        servername = config.getString("rabbitmq.servername");
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

    public String getConsoleSpy(){
        return config.getString("console.spy");
    }



}
