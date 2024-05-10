package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.JavaPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.common.plugin.AbstractAurionChat;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;

public class AurionChat extends AbstractAurionChat {

    public final Bootstrap bootstrap;

    public BukkitAudiences audiences;

    public SenderFactory senderFactory;

    private CommandExecutor commandManager;

    public AurionChat(Bootstrap bootstrap){
        super(new JavaPluginLogger(Bukkit.getLogger()));
        this.bootstrap = bootstrap;
    }

    @Override
    protected void registerPlatformListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(this), getBootstrap());
        pluginManager.registerEvents(new ChatListener(this), getBootstrap());
    }

    @Override
    protected void setupSenderFactory() {
        this.audiences = BukkitAudiences.create(getBootstrap());
        this.senderFactory = new SenderFactory(this);
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new BukkitConfigurationAdapter(resolveConfig("config.yml").toFile());
    }

    public SenderFactory getSenderFactory(){
        return this.senderFactory;
    }

    @Override
    public Bootstrap getBootstrap(){
        return this.bootstrap;
    }

    public Server getServer(){
        return bootstrap.getServer();
    }

    @Override
    protected void registerCommands() {
        PluginCommand command = bootstrap.getCommand("chat");
        if(command == null){
            getLogger().severe("Unable to register /chat command with the server");
            return;
        }

        this.commandManager = new CommandExecutor(this, command);
        this.commandManager.register();
    }

    @Override
    protected void disablePlugin() {
        getServer().getPluginManager().disablePlugin(getBootstrap());
    }

    @Override
    public PluginLogger getLogger() {
        return new JavaPluginLogger(Bukkit.getLogger());
    }

    public BukkitAudiences getAudiences(){
        return audiences;
    }
}