package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.sponge.channel.ChatService;
import com.mineaurion.aurionchat.sponge.command.CommandManager;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.CommandListener;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
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
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "aurionchat",
        name = "Aurionchat",
        url = "https://mineaurion.com",
        description = "Chat accross server with rabbitmq",
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


    public static final String CHANNEL = "aurionchat";

    public static Optional<LuckPermsApi> luckPermsApi;

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
        setupChannels(config);
        luckPermsApi = Sponge.getPluginManager().getPlugin("luckperms").isPresent() ? LuckPerms.getApiSafe() : Optional.empty();
        sendConsoleMessage("&8[&eAurionChat&8]&e - Rabbitmq & Channels Loaded.");
        loadCommands(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Commands Loaded.");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        getLogger().info("Successfully running AurionChat - Debug");
    }


    public void setupListeners(){
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListeners(this, new LoginListener(this));
        eventManager.registerListeners(this, new ChatListener(this));
        eventManager.registerListeners(this, new CommandListener(this));
    }
    public void setupChannels(Config config){
        try{
            getChatService().join(config.rabbitmq.servername);
        }
        catch (Exception e){
            getLogger().error(e.getMessage());
        }
    }

    public void loadCommands(AurionChat plugin){
        CommandManager commandManager = new CommandManager(plugin);
        Sponge.getCommandManager().register(plugin, commandManager.cmdChat, "channel", "ch");
    }

    public ChatService getChatService(){
        ChatService chatService = null;
        config = getConfig();
        try{
            chatService = new ChatService(config.rabbitmq.uri, this);
        }
        catch(Exception e){
            getLogger().error("Connection error with the rabbitmq instance");
            e.printStackTrace();
            Sponge.getEventManager().unregisterListeners(this);
            Sponge.getCommandManager().getOwnedBy(this).forEach(Sponge.getCommandManager()::removeMapping);
            Sponge.getScheduler().getScheduledTasks(this).forEach(Task::cancel);
        }
        return chatService;
    }

    public void sendConsoleMessage(String message){

        Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
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

    public AurionChatPlayers getAurionChatPlayers(){
        return this.aurionChatPlayers;
    }

}
