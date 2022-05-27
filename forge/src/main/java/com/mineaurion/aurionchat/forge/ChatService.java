package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.channel.ChatServiceCommon;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatService extends ChatServiceCommon {
    public ChatService(String uri, String queueName) throws IOException, TimeoutException {
        super(uri, queueName);
    }

    @Override
    public void sendMessage(String channel, String message){
        AurionChat.utils.sendMessageToPlayer(channel, message);
        if(AurionChat.config.options.spy.get()){
            // send console message
            LogManager.getLogger().info(message);
        }
    }

    @Override
    public void sendAutoMessage(String channel, String message){
        if(AurionChat.config.options.autoMessage.get()){
            AurionChat.utils.broadcastToPlayer(channel, message);
        }
    }
}
