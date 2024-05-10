package com.mineaurion.aurionchat.common.plugin;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.logger.PluginLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractAurionChat implements AurionChatPlugin {

    private final PluginLogger logger;

    private Map<UUID, AurionChatPlayer> aurionChatPlayers;

    private ChatService chatService;

    public AbstractAurionChat(PluginLogger logger){
        this.logger = logger;
    }


    public final void enable(){
        logger.info(AurionChatPlugin.NAME + " starting");
        //send message for startup
        try {
            aurionChatPlayers = new HashMap<>();
            chatService = new ChatService(this);
            logger.info("AurionChat Connected to Rabbitmq");
            setupSenderFactory();
            registerPlatformListeners(); // if no error , init of the "plugin"
            registerCommands();
        } catch (IOException e) {
            logger.severe("Could not enable AurionChat", e);
            // perform action in case of error
            disablePlugin();
        }
    }

    public final void disable(){
        logger.info("AurionChat is shutting down");
        if(!chatService.connected){
            chatService.close();
        }
        logger.info("Aurionchat shutdown complete");
    }

    protected Path resolveConfig(String filename){
        Path configFile = getBootstrap().getConfigDirectory().resolve(filename);

        if(!Files.exists(configFile)){
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException e){
                //ignore
            }

            try(InputStream is = getBootstrap().getResourceStream(filename)){
                Files.copy(is, configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return configFile;
    }

    public ChatService getChatService(){
        return chatService;
    }

    public Map<UUID,AurionChatPlayer> getAurionChatPlayers() {
        return aurionChatPlayers;
    }

    protected abstract void registerPlatformListeners();

    protected abstract void setupSenderFactory();

    // protected abstract void setupPlayerFactory();

    protected abstract void registerCommands();

    protected abstract void disablePlugin();

    @Override
    public PluginLogger getLogger(){
        return this.logger;
    }

}
