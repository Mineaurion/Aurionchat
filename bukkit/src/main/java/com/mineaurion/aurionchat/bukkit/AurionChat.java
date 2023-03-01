package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.logger.JavaPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AurionChat extends AbstractAurionChat<AurionChatPlayer> {

    public static final String ID = "aurionchat";

    public static Config config;

    public final JavaPlugin plugin;

    public AurionChat(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void onEnable(){
        getlogger().info("AurionChat Initializing");
        config = new Config(this.plugin);
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
        PluginManager pluginManager = this.plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(this), this.plugin);
        pluginManager.registerEvents(new CommandListener(this), this.plugin);
        pluginManager.registerEvents(new ChatListener(this), this.plugin);
    }

    @Override
    protected void registerCommands() {
        this.plugin.getCommand("chat").setExecutor(new ChatCommand(this));
    }

    @Override
    protected void disablePlugin() {
        this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
    }

    @Override
    public PluginLogger getlogger() {
        return new JavaPluginLogger(this.plugin.getServer().getLogger());
    }
}