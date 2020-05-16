package com.mineaurion.aurionchat.bukkit.channel;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.Config;
import com.mineaurion.aurionchat.bukkit.Utils;
import org.bukkit.Bukkit;


import com.mineaurion.aurionchat.common.channel.ChatServiceCommun;
import org.bukkit.ChatColor;

import java.util.Set;
import java.util.regex.Pattern;

public class ChatService extends ChatServiceCommun {

    private AurionChat plugin;
    private Config config;
    private Utils utils;

    public ChatService(String uri, AurionChat plugin){
        super(uri);
        this.plugin = plugin;
        this.config = plugin.getConfigPlugin();
        this.utils = plugin.getUtils();
    }

    @Override
    public void sendMessage(String channel, String message){
        utils.sendMessageToPlayer(channel, message);
        if(plugin.getConfigPlugin().getConsoleSpy()){
            plugin.sendConsoleMessage(message);
        }

    }

    @Override
    public void sendAutoMessage(String channel, String message) {
        if(config.getAutomessageEnable()){
            utils.broadcastToPlayer(channel, message);
        }
    }

    @Override
    public void desactivatePlugin(){
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}
