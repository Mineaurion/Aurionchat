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
    public void sendMessage(String channelName, String message){
        String channel = channelName.toLowerCase();
        String messageClean = message.replaceFirst(Pattern.quote(channel + " "), "");
        //#TODO a check
        if(config.getAutomessageEnable()){
            Set<String> automessageChannels = config.getAllAutomessageChannel();
            if(automessageChannels.contains(channel)){
                utils.broadcastToPlayer(channel, messageClean);
            }
        }
        utils.sendMessageToPlayer(channel, messageClean);
        if(plugin.getConfigPlugin().getConsoleSpy()){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',messageClean));
        }

    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }

    @Override
    public void desactivatePlugin(){
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}
