package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import com.mineaurion.aurionchat.sponge.channel.ChatService;
import com.mineaurion.aurionchat.sponge.command.CommandManager;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.CommandListener;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import net.luckperms.api.LuckPerms;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
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
import org.spongepowered.api.service.ProviderRegistration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.mineaurion.aurionchat.sponge.Utils.sendConsoleMessage;

@Plugin(
        id = "aurionchat",
        name = "Aurionchat",
        url = "https://mineaurion.com",
        description = "Chat across server with rabbitmq",
        authors = {
                "Yann151924"
        },
        dependencies = {
            @Dependency(id= "luckperms", optional = true)
        }
)
public class AurionChat {

    @Inject @DefaultConfig(sharedRoot = true)
    public Path path;

    @Inject @DefaultConfig(sharedRoot = true)
    ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    Game game;

    @Inject
    public static Logger logger;

    public static Config config;
    public static Map<UUID, AurionChatPlayer> aurionChatPlayers;
    public static LuckPermsUtils luckPermsUtils = null;
    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }
    private ChatService chatService;

    @Listener
    public void Init(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        Optional<ProviderRegistration<LuckPerms>> provider = Sponge.getServiceManager().getRegistration(LuckPerms.class);
        provider.ifPresent(luckPermsProviderRegistration -> luckPermsUtils = new LuckPermsUtils(luckPermsProviderRegistration.getProvider()));
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
        sendConsoleMessage("&8[&eAurionChat&8]&e - Config Loaded.");
        setupListeners();
        sendConsoleMessage("&8[&eAurionChat&8]&e - Listener Loaded.");

        try{
            this.setChatService(new ChatService(config.rabbitmq.uri, config.rabbitmq.servername));
            sendConsoleMessage("&8[&eAurionChat&8]&e - Rabbitmq & Channels Loaded.");
            CommandManager commandManager = new CommandManager();
            Sponge.getCommandManager().register(this, commandManager.cmdChat, "channel", "ch");
            sendConsoleMessage("&8[&eAurionChat&8]&e - Commands Loaded.");
        } catch (IOException|TimeoutException e){
            Sponge.getEventManager().unregisterListeners(this);
            Sponge.getCommandManager().getOwnedBy(this).forEach(Sponge.getCommandManager()::removeMapping);
            Sponge.getScheduler().getScheduledTasks(this).forEach(Task::cancel);
            sendConsoleMessage("&8[&eAurionChat&8]&e - &ccan't connect to rabbitmq, disabling.");
            logger.error(e.getMessage());
        }
    }

    @Listener
    public void onServerStop(GameStoppingEvent event){
        try {
            this.getChatService().close();
        } catch (TimeoutException|IOException e) {
            sendConsoleMessage("&8[&eAurionChat&8]&e - Error when communication closed");
           logger.error(e.getMessage());
        }

    }

    public void setupListeners(){
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListeners(this, new LoginListener());
        eventManager.registerListeners(this, new ChatListener(this));
        eventManager.registerListeners(this, new CommandListener(this));
    }
}
