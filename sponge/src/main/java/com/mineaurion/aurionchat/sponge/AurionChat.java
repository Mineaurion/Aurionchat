package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import com.mineaurion.aurionchat.sponge.channel.ChatService;
import com.mineaurion.aurionchat.sponge.command.CommandManager;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.CommandListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

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
    private Logger logger;

    private Config config;
    private AurionChatPlayers aurionChatPlayers;
    private Utils utils;
    private ChatService chatService;
    private LuckPermsUtils luckPermsUtils = null;

    public static LuckPerms luckPermsApi = null;

    @Listener
    public void Init(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        if(!Files.exists(path)){
            game.getAssetManager().getAsset(this,"config.conf").get().copyToFile(path);
        }
        config = loader.load().getValue(Config.type);
        aurionChatPlayers = new AurionChatPlayers();
        utils = new Utils(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Config Loaded.");
        setupListeners();
        sendConsoleMessage("&8[&eAurionChat&8]&e - Listener Loaded.");
        chatService = new ChatService(config.rabbitmq.uri, this);
        try{
            chatService.join(config.rabbitmq.servername);
        }
        catch (IOException e){
            getLogger().error(e.getMessage());
        }
        sendConsoleMessage("&8[&eAurionChat&8]&e - Rabbitmq & Channels Loaded.");
        Optional<ProviderRegistration<LuckPerms>> provider = Sponge.getServiceManager().getRegistration(LuckPerms.class);
        provider.ifPresent(luckPermsProviderRegistration -> luckPermsUtils = new LuckPermsUtils(luckPermsProviderRegistration.getProvider()));
        loadCommands(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Commands Loaded.");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        getLogger().info("Successfully running AurionChat");
    }

    @Listener
    public void onServerStop(GameStoppingEvent event) throws IOException, TimeoutException {
        chatService.leave(config.rabbitmq.servername);
        chatService.close();
    }


    public void setupListeners(){
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListeners(this, new LoginListener(this));
        eventManager.registerListeners(this, new ChatListener(this));
        eventManager.registerListeners(this, new CommandListener(this));
    }


    public void loadCommands(AurionChat plugin){
        CommandManager commandManager = new CommandManager(plugin);
        Sponge.getCommandManager().register(plugin, commandManager.cmdChat, "channel", "ch");
    }

    public void sendConsoleMessage(String message){
        Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

    public ChatService getChatService(){
        return  this.chatService;
    }

    public Logger getLogger(){
        return logger;
    }

    public Config getConfig(){
        return this.config;
    }

    public Utils getUtils(){
        return this.utils;
    }

    public LuckPermsUtils getLuckPermsUtils() { return this.luckPermsUtils; }

    public AurionChatPlayers getAurionChatPlayers(){
        return this.aurionChatPlayers;
    }

}
