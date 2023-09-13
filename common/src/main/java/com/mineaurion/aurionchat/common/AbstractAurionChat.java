package com.mineaurion.aurionchat.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.rabbitmq.client.DeliverCallback;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPermsProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractAurionChat<T extends AurionChatPlayerCommon<?>> implements AurionChatPlugin {

    public Map<UUID,T> getAurionChatPlayers() {
        return aurionChatPlayers;
    }

    private Map<UUID, T> aurionChatPlayers;

    private ChatService chatService;

    public static LuckPermsUtils luckPermsUtils = null;

    private final PluginLogger logger = getlogger();

    public final void enable(String uri, boolean spy, boolean autoMessage, boolean withLuckPerms){
        //send message for startup
        try {
            aurionChatPlayers = new HashMap<>();
            chatService = new ChatService(uri, consumer(autoMessage, spy));
            logger.info("AurionChat Connected to Rabbitmq");
            registerPlatformListeners(); // if no error , init of the "plugin"
            registerCommands();
            if(withLuckPerms){
                luckPermsUtils = new LuckPermsUtils(LuckPermsProvider.get());
            }
        } catch (IOException e) {
            // perform action in case of error
            disablePlugin();
        }
    }

    public final void disable(){
        logger.info("AurionChat is shutting down");
        if(!chatService.connected){
            chatService.close();
        }
    }

    protected abstract void registerPlatformListeners();
    protected abstract void registerCommands();

    protected abstract void disablePlugin();

    private DeliverCallback consumer(boolean autoMessage, boolean spy){
        return (consumerTag, delivery) -> {
            JsonObject json = new JsonParser().parse(new String(delivery.getBody(), StandardCharsets.UTF_8)).getAsJsonObject();

            String channel = json.get("channel").getAsString();
            String message = json.get("message").getAsString();
            String type = json.get("type").getAsString();
            Component messageDeserialize = MiniMessage.miniMessage().deserialize(message);

            getAurionChatPlayers().forEach((uuid, aurionChatPlayers) -> {
                if(type.equalsIgnoreCase("automessage") && autoMessage){
                    if(aurionChatPlayers.hasPermission("aurionchat.automessage." + channel)){
                        aurionChatPlayers.sendMessage(messageDeserialize);
                    }
                } else if (type.equalsIgnoreCase("chat")) {
                    if(spy){
                        logger.info(message);
                    }
                    if(aurionChatPlayers.getChannels().contains(channel)){
                        aurionChatPlayers.sendMessage(messageDeserialize);
                    }
                } else {
                    logger.warn("Received message with the type " + type + " and the message was " + message + ". It won't be processed");
                }
            });
        };
    }
}
