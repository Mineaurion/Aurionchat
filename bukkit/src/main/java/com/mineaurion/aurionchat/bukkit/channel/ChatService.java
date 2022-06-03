package com.mineaurion.aurionchat.bukkit.channel;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.channel.ChatServiceCommon;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatService extends ChatServiceCommon {
    public ChatService(String uri, String queueName) throws IOException, TimeoutException {
        super(uri, queueName);
    }

    @Override
    public void sendMessage(String channel, String message){
        AurionChat.utils.sendMessageToPlayer(channel, message);
        if(AurionChat.config.options.spy){
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    @Override
    public void sendAutoMessage(String channel, String message) {
        if(AurionChat.config.options.autoMessage){
            AurionChat.utils.broadcastToPlayer(channel, message);
        }
    }
}
