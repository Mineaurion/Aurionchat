package com.mineaurion.aurionchat.common;

import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.common.player.PlayerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractAurionChat implements AurionChatPlugin {

    public static final String ID = "aurionchat";

    private Map<UUID, AurionChatPlayer> aurionChatPlayers;

    private ChatService chatService;

    private final PluginLogger logger = getlogger();

    public final void enable(){
        //send message for startup
        try {
            aurionChatPlayers = new HashMap<>();
            chatService = new ChatService(this);
            logger.info("AurionChat Connected to Rabbitmq");
            setupPlayerFactory();
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
        Path configFile = getConfigDirectory().resolve(filename);

        if(!Files.exists(configFile)){
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException e){
                //ignore
            }

            try(InputStream is = getRessourceStream(filename)){
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

    @Override
    public String getServerName() {
        return getConfigurationAdapter().getString("server_name", "AurionChat#" + Long.toHexString(System.currentTimeMillis()));
    }

    protected abstract void registerPlatformListeners();

    protected abstract void setupPlayerFactory();

    protected abstract void registerCommands();

    protected abstract void disablePlugin();

    public abstract ConfigurationAdapter getConfigurationAdapter();

    public abstract PlayerFactory<?> getPlayerFactory();

    /**
     * Gets the plugins main data storage directory
     *
     * <p>Bukkit: ./plugins/Economy</p>
     * <p>Sponge: ./Economy/</p>
     * <p>Fabric: ./mods/Economy</p>
     * <p>Forge: ./config/Economy</p>
     *
     * @return the platforms data folder
     */
    protected abstract Path getConfigDirectory();

    private InputStream getRessourceStream(String path){
        return getClass().getClassLoader().getResourceAsStream(path);
    }

}
