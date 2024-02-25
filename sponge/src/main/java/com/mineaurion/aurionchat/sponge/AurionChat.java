package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.sponge.command.ChatCommand;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.PlayerListener;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin("aurionchat")
public class AurionChat extends AbstractAurionChat {

    private PlayerFactory playerFactory;

    @Inject
    public PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;

    @Listener
    public void Init(ConstructPluginEvent event) {
        getlogger().info("AurionChat Initializing");
        this.enable();
    }

    @Listener
    public void onServerStop(StoppingEngineEvent<Server> event){
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        EventManager eventManager = Sponge.eventManager();
        eventManager.registerListeners(this.container, new PlayerListener(this));
        eventManager.registerListeners(this.container, new ChatListener(this));
    }

    @Override
    protected void setupPlayerFactory() {
        this.playerFactory = new PlayerFactory();
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
    public ConfigurationAdapter getConfigurationAdapter() {
        return new SpongeConfigAdapter(resolveConfig());
    }

    @Override
    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    private Path resolveConfig() {
        Path path = getConfigDirectory().resolve(AurionChat.ID + ".conf");
        if (!Files.exists(path)) {
            try {
                createDirectoriesIfNotExists(getConfigDirectory());
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(AurionChat.ID + ".conf")) {
                    Files.copy(is, path);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return path;
    }

    public static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return;
        }

        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            // ignore
        }
    }

    @Override
    protected Path getConfigDirectory() {
        return configDirectory;
    }

    @Override
    public PluginLogger getlogger() {
        return new Log4jPluginLogger(LogManager.getLogger(AurionChat.ID));
    }
}
