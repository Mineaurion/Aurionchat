package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.logger.JavaPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AurionChat extends AbstractAurionChat<AurionChatPlayer> {

    public static final String ID = "aurionchat";

    public static Config config;

    public final JavaPlugin plugin;

    public static BukkitAudiences audiences;

    public AurionChat(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void onEnable(){
        getlogger().info("AurionChat Initializing");
        audiences = BukkitAudiences.create(plugin);
        config = new Config(plugin);
        this.enable(
                config.rabbitmq.uri,
                config.rabbitmq.serverName,
                config.options.spy,
                config.options.autoMessage,
                true
        );
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
}