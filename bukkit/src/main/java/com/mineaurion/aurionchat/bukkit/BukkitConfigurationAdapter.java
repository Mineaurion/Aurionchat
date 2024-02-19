package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BukkitConfigurationAdapter implements ConfigurationAdapter {

    private final AurionChat plugin;
    private final File file;
    private YamlConfiguration configuration;
    private @Nullable AurionPlaceholderAPI apapi;

    public BukkitConfigurationAdapter(AurionChat plugin, File file){
        this.plugin = plugin;
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

    @Override
    public String wrapString(@Nullable UUID player, String str) {
        return apapi == null || player == null
                ? ConfigurationAdapter.super.wrapString(player, str)
                : apapi.onRequest(Bukkit.getOfflinePlayer(player), str);
    }

    public void initPlaceholderApi() {
        apapi = new AurionPlaceholderAPI(plugin);
        PlaceholderAPI.registerExpansion(apapi);
    }
}
