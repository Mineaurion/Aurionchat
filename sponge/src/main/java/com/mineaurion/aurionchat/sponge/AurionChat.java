package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.sponge.command.ChatCommand;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "aurionchat",
        name = "Aurionchat",
        url = "https://mineaurion.com",
        description = "Chat across server with rabbitmq",
        authors = {
                "Yann151924"
        },
        version = "@projectVersion@",
        dependencies = {
            @Dependency(id= "luckperms")
        }
)
public class AurionChat extends AbstractAurionChat<AurionChatPlayer> {

    public static final String ID = "aurionchat";
    @Inject @DefaultConfig(sharedRoot = true)
    public Path path;
    @Inject @DefaultConfig(sharedRoot = true)
    ConfigurationLoader<CommentedConfigurationNode> loader;
    @Inject
    Game game;

    public static Config config;

    @Listener
    public void Init(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        getlogger().info("AurionChat Initializing");
        if(!Files.exists(path)){
            game.getAssetManager().getAsset(this, "config.conf").ifPresent(asset -> {
                try {
                    asset.copyToFile(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        config = loader.load().getValue(Config.type);
        this.enable(
                config.rabbitmq.uri,
                config.rabbitmq.servername,
                config.options.spy,
                config.options.automessage,
                true
        );
    }


    @Listener
    public void onServerStop(GameStoppingEvent event){
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListeners(this, new LoginListener(this));
        eventManager.registerListeners(this, new ChatListener(this));
    }

    @Override
    protected void registerCommands() {
        new ChatCommand(this, Sponge.getCommandManager());
    }

    @Override
    protected void disablePlugin() {
        Sponge.getEventManager().unregisterListeners(this);
        Sponge.getCommandManager().getOwnedBy(this).forEach(Sponge.getCommandManager()::removeMapping);
        Sponge.getScheduler().getScheduledTasks(this).forEach(Task::cancel);
    }

    @Override
    public PluginLogger getlogger() {
        return new Log4jPluginLogger(LogManager.getLogger(AurionChat.ID));
    }
}
