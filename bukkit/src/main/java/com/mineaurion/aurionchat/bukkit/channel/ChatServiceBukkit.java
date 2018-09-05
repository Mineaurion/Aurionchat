package com.mineaurion.aurionchat.bukkit.channel;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import org.bukkit.Bukkit;


import com.mineaurion.aurionchat.common.channel.ChatService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatServiceBukkit extends ChatService {

    private AurionChat plugin;

    public ChatServiceBukkit(String host, AurionChat plugin) throws IOException, TimeoutException {
        super(host);
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(String channelName, String message){
        plugin.getUtils().sendMessageToPlayer(channelName,message.replace(channelName + " ",""));
        if(plugin.getConfigPlugin().consoleSpy.equalsIgnoreCase("true")){
        Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }
}
