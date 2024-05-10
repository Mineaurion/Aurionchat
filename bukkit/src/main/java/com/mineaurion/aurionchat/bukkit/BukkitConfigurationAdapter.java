package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BukkitConfigurationAdapter implements ConfigurationAdapter {

    private final File file;
    private YamlConfiguration configuration;

    public BukkitConfigurationAdapter(File file){
        this.file = file;
        reload();
    }

    @Override
    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public String getString(String path, String def) {
        return this.configuration.getString(path, def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return this.configuration.getBoolean(path, def);
    }

    public Map<String, Channel> getChannels(){
        Map<String, Channel> map = new HashMap<>();
        ConfigurationSection section = this.configuration.getConfigurationSection("channels");
        if(section == null){
            return new HashMap<>();
        }

        for(String channel: this.configuration.getConfigurationSection("channels").getKeys(false)){
            map.putIfAbsent(
                    channel,
                    new Channel(
                            this.configuration.getString("channels." + channel + ".format"),
                            this.configuration.getString("channels." + channel + ".alias"),
                            stringListToUrlMode(this.configuration.getStringList("channels." + channel + ".url_mode"))
                    )
            );
        }
        return map;
    }
}
