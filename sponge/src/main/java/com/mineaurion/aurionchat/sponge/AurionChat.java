package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.plugin.AbstractAurionChat;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class AurionChat extends AbstractAurionChat {

    public final Bootstrap bootstrap;

    private SenderFactory senderFactory;
    private CommandExecutor commandManager;

    public AurionChat(Bootstrap bootstrap){
        super(bootstrap.getLogger());
        this.bootstrap = bootstrap;
    }

    @Override
    protected void registerPlatformListeners() {
        EventManager eventManager = Sponge.eventManager();
        eventManager.registerListeners(this.bootstrap.getPluginContainer(), new LoginListener(this));
        eventManager.registerListeners(this.bootstrap.getPluginContainer(), new ChatListener(this));
    }

    @Override
    protected void registerCommands() {
        this.commandManager = new CommandExecutor(this);
        this.bootstrap.registerListeners(new RegisterCommandsListener(this.bootstrap.getPluginContainer(), this.commandManager));
    }

    @Override
    protected void disablePlugin() {
        Sponge.eventManager().unregisterListeners(this);
    }

    public static final class RegisterCommandsListener {
        private final PluginContainer pluginContainer;
        private final Command.Raw command;
        RegisterCommandsListener(PluginContainer pluginContainer, Command.Raw command){
            this.pluginContainer = pluginContainer;
            this.command = command;
        }
        @Listener
        public void onCommandRegister(RegisterCommandEvent<Command.Raw> event){
            event.register(this.pluginContainer, this.command, "aurionchat", "chat", "c");
        }
    }

    public SenderFactory getSenderFactory(){
        return this.senderFactory;
    }

    public Optional<Server> getServer(){
        return this.bootstrap.getServer();
    }

    @Override
    protected void setupSenderFactory() {
        this.senderFactory = new SenderFactory(this);
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new SpongeConfigAdapter(resolveConfig());
    }

    private Path resolveConfig() {
        Path path = this.bootstrap.getConfigDirectory().resolve(AurionChatPlugin.MOD_ID + ".conf");
        if (!Files.exists(path)) {
            try {
                Bootstrap.createDirectoriesIfNotExists(this.bootstrap.getConfigDirectory());
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(AurionChatPlugin.MOD_ID + ".conf")) {
                    Files.copy(is, path);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return path;
    }

    @Override
    public Bootstrap getBootstrap() {
        return this.bootstrap;
    }
}
