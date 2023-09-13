package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.sponge.command.ChatCommand;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin("aurionchat")
public class AurionChat extends AbstractAurionChat<AurionChatPlayer> {

    public static final String ID = "aurionchat";

    @Inject
    public PluginContainer container;
    @Inject @DefaultConfig(sharedRoot = true)
    public Path path;
    @Inject
    ConfigurationLoader<CommentedConfigurationNode> loader;
    @Inject
    Game game;

    public static Config config;

    @Listener
    public void Init(ConstructPluginEvent event) throws IOException {
        getlogger().info("AurionChat Initializing");
        ConfigurationReference<CommentedConfigurationNode> configurationReference = HoconConfigurationLoader.builder().path(path).build().loadToReference();
        ValueReference<Config, CommentedConfigurationNode> configRef = configurationReference.referenceTo(Config.class);

        if(!Files.exists(path)) {
            configurationReference.save();
        } else {
            configurationReference.load();
        }
        config = configRef.get();

        this.enable(
                config.rabbitmq.uri,
                config.rabbitmq.servername,
                config.options.spy,
                config.options.automessage,
                true
        );
    }

    @Listener
    public void onServerStop(StoppingEngineEvent<Server> event){
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        EventManager eventManager = Sponge.eventManager();
        eventManager.registerListeners(this.container, new LoginListener(this));
        eventManager.registerListeners(this.container, new ChatListener(this));
    }

    @Override
    protected void registerCommands() {
        // look #onCommandRegister
    }

    @Listener
    public void onCommandRegister(RegisterCommandEvent<Command.Parameterized> event){
        new ChatCommand(this, event);
    }

    @Override
    protected void disablePlugin() {
        Sponge.eventManager().unregisterListeners(this);
    }

    @Override
    public PluginLogger getlogger() {
        return new Log4jPluginLogger(LogManager.getLogger(AurionChat.ID));
    }
}
