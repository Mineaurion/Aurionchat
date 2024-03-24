package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.JavaPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class AurionChat extends AbstractAurionChat {

    public final JavaPlugin plugin;

    public BukkitAudiences audiences;

    private PlayerFactory playerFactory;

    public AurionChat(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void onEnable(){
        getlogger().info("AurionChat Initializing");
        audiences = BukkitAudiences.create(plugin);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            ((BukkitConfigurationAdapter) getConfigurationAdapter()).initPlaceholderApi();
        this.enable();
    }

    public void onDisable() {
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(this), plugin);
        pluginManager.registerEvents(new CommandListener(this), plugin);
        pluginManager.registerEvents(new ChatListener(this), plugin);
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new BukkitConfigurationAdapter(this, resolveConfig("config.yml").toFile());
    }

    @Override
    protected Path getConfigDirectory() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    protected void registerCommands() {
        plugin.getCommand("chat").setExecutor(new ChatCommand(this));
    }

    @Override
    protected void disablePlugin() {
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    @Override
    public PluginLogger getlogger() {
        return new JavaPluginLogger(Bukkit.getLogger());
    }

    public BukkitAudiences getAudiences(){
        return audiences;
    }

    @Override
    protected void setupPlayerFactory() {
        this.playerFactory = new PlayerFactory(this);
    }

    @Override
    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }
}