package com.mineaurion.aurionchat.sponge;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.sponge.channel.ChatServiceSponge;
import com.mineaurion.aurionchat.sponge.listeners.LoginListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.serializer.TextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Plugin(
        id = "aurionchat",
        name = "Aurionchat",
        url = "https://mineaurion.com",
        authors = {
                "Yann151924"
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


    @Listener
    public void Init(GamePreInitializationEvent event) {
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        config = getConfig();
        sendConsoleMessage("&8[&eAurionChat&8]&e - Config Loaded.");
        setupListeners();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        logger.info("Successfully running AurionChat - Debug");
    }


    public void setupListeners(){
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListeners(this, new LoginListener(this));
    }


    public ChatServiceSponge getChatService(){
        ChatServiceSponge chatService = null;
        try{
            chatService = new ChatServiceSponge("sponge1", this);
        }
        catch(IOException | TimeoutException e){
            e.printStackTrace();
        }
        return chatService;
    }

    public Config getConfig(){
        try{
            configReturn = new Config(this);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return configReturn;
    }


    public void sendConsoleMessage(String message){
        Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

}
