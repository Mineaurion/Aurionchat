package com.mineaurion.aurionchat.bukkit.channel;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.Utils;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatService extends com.mineaurion.aurionchat.common.channel.ChatService {

    public ChatService(String uri, String queueName) throws IOException, TimeoutException {
        super(uri, queueName);
    }

    @Override
    public void sendMessage(String channel, String message){
        Utils.sendMessageToPlayer(channel, message);
        if(AurionChat.config.getConsoleSpy()){
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    @Override
    public void sendAutoMessage(String channel, String message) {
        if(AurionChat.config.getAutomessageEnable()){
            Utils.broadcastToPlayer(channel, message);
        }
    }
}
