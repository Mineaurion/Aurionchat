package com.mineaurion.aurionchat.common;

import com.google.gson.JsonParser;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.rabbitmq.client.DeliverCallback;
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

    public final void enable(String uri, String serverName, boolean spy, boolean autoMessage, boolean withLuckPerms){

        //send message for startup
        try {
            aurionChatPlayers = new HashMap<>();
            chatService = new ChatService(uri, serverName, consumer(autoMessage, spy));
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
            String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] routingKeySplit = delivery.getEnvelope().getRoutingKey().split("\\.");

            String message = new JsonParser().parse(json).getAsJsonObject().get("message").getAsString();
            String type = routingKeySplit[1];
            String channel = routingKeySplit[2].toLowerCase();

            if(type.equalsIgnoreCase("automessage")){
                if(autoMessage){
                  Utils.broadcastToPlayer(channel, message, getAurionChatPlayers());
                }
            } else if(type.equalsIgnoreCase("chat")) {
                if(spy){
                    logger.info(message); // TODO: need to be rework for better log
                }
                Utils.sendMessageToPlayer(channel, message, getAurionChatPlayers());
            } else {
                logger.warn("Received message with the type " + type + " and the message was " + message + ". It won't be processed");
            }
        };

    }
}
