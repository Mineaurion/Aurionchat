package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.sponge.channel.ChatServiceSponge;
import com.mineaurion.aurionchat.sponge.command.CommandManager;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import com.mineaurion.aurionchat.sponge.listeners.ChatListener;
import com.mineaurion.aurionchat.sponge.listeners.CommandListener;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.serializer.TextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Plugin(
        id = "aurionchat",
        name = "Aurionchat",
        url = "https://mineaurion.com",
        authors = {
                "Yann151924"
        },
        dependencies = {
            @Dependency(id= "luckperms", optional = true)
        }
)
public class AurionChat {

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path defaultConf;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public File defaultConfigFile;

    @Inject
    public PluginContainer pluginContainer;


    @Inject
    private Logger logger;

    private Config config;
    private Config configReturn;

    public static final String CHANNEL = "aurionchat";
    public static Set<AurionChatPlayer> players = new HashSet<>();
    public static Set<AurionChatPlayer> onlinePlayers = new HashSet<>();

    public static Optional<LuckPermsApi> luckPermsApi;

    @Listener
    public void Init(GamePreInitializationEvent event) {
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        config = getConfig();
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
            getChatService().join(config.getServername());
        }
        catch (Exception e){
            getLogger().error(e.getMessage());
        }
    }

    public void loadCommands(AurionChat plugin){
        CommandManager commandManager = new CommandManager(plugin);
        Sponge.getCommandManager().register(plugin, commandManager.cmdChat, "channel", "ch");
    }

    public ChatServiceSponge getChatService(){
        ChatServiceSponge chatService = null;
        config = getConfig();
        try{
            chatService = new ChatServiceSponge(config.getUri(), this);
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

    public Config getConfig(){
        try{
            configReturn = new Config(this);
        }
        catch (Exception e){
            getLogger().error(e.getMessage());
        }
        return configReturn;
    }

    public Utils getUtils(){
        return new Utils(this);
    }

    public void sendConsoleMessage(String message){

        Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

    public Logger getLogger(){
        return logger;
    }

}
