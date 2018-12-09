package com.mineaurion.aurionchat.sponge.channel;

import com.mineaurion.aurionchat.common.channel.ChatServiceCommun;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import com.mineaurion.aurionchat.sponge.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class ChatService extends ChatServiceCommun {
    private AurionChat plugin;
    private Config config;
    private Utils utils;

    public ChatService(String uri, AurionChat plugin) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        super(uri);
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.utils = plugin.getUtils();
    }

    @Override
    public void sendMessage(String channelName, String message){
        String channel = channelName.toLowerCase();
        String messageClean = message.replace(channelName + " ", "");
        //#TODO a check
        if(config.options.automessage){
            Set<String> automessageChannels = config.channels.keySet();
            if(automessageChannels.contains(channel)){
                utils.broadcastToPlayer(channel, message);
            }
        }
        utils.sendMessageToPlayer(channel, messageClean);
        if(config.options.spy){
            plugin.sendConsoleMessage(messageClean);
        }
    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }
}
