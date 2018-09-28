package com.mineaurion.aurionchat.bukkit.channel;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.Config;
import com.mineaurion.aurionchat.bukkit.Utils;
import org.bukkit.Bukkit;


import com.mineaurion.aurionchat.common.channel.ChatService;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class ChatServiceBukkit extends ChatService {

    private AurionChat plugin;
    private Config config;
    private Utils utils;

    public ChatServiceBukkit(String uri, AurionChat plugin) throws Exception {
        super(uri);
        this.plugin = plugin;
        this.config = plugin.getConfigPlugin();
        this.utils = plugin.getUtils();
    }

    @Override
    public void sendMessage(String channelName, String message){
        String messageClean = message.replace(channelName + " ", "");
        if(config.getAutomessageEnable()){
            Set<String> automessageChannels = config.getAllAutomessageChannel();
            if(automessageChannels.contains(channelName)){
                utils.broadcastToPlayer(channelName, messageClean);
            }
        }
        utils.sendMessageToPlayer(channelName, messageClean);
        if(plugin.getConfigPlugin().getConsoleSpy().equalsIgnoreCase("true")){
            Bukkit.getConsoleSender().sendMessage(messageClean);
        }

    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }
}
